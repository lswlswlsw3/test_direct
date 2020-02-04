package com.sicc.order.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sicc.order.adapter.ParticipantLink;
import com.sicc.order.adapter.ParticipationRequest;
import com.sicc.order.adapter.TccRestAdapter;
import com.sicc.order.vo.OrderVO;

/**
 * 주문 service 구현체
 * @author Woongs
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private TccRestAdapter tccRestAdapter;

    @Autowired
    public void setTccRestAdapter(TccRestAdapter tccRestAdapter) {
        this.tccRestAdapter = tccRestAdapter;
    }

    @Override
    public void placeOrder(final OrderVO orderVO) {
        log.info("Place Order ...");

        // REST Resources 생성
        // 재고 차감 요청
        ParticipationRequest stockParticipationRequest = reduceStock(orderVO);

        // 결제 요청
        ParticipationRequest paymentParticipationRequest = payOrder(orderVO);

        // 1. TCC - Try
        List<ParticipantLink> participantLinks  =
                tccRestAdapter.doTry(Arrays.asList(stockParticipationRequest, paymentParticipationRequest));

        // Exception Path
        // ymyoo.order.controller.OrderRestControllerIntegrationTest.placeOrder_TCC_TRY는_모두_성공했지만_내부_오류로_인해_TCC_Confirm_하지_않는_경우
        if(orderVO.getProductId().equals("prd-0002")) {
            throw new RuntimeException("Error Before Confirm...");
        }

        // Exception Path
        // ymyoo.order.controller.OrderRestControllerIntegrationTest.placeOrder_TCC_TRY는_모두_성공했지만_내부_로직_수행_시간이_너무_오래_걸려_TCC_Confirm_중_TIMEOUT_되는_경우
        if(orderVO.getProductId().equals("prd-0003")) {
            try {
                waitCurrentThread(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 2. TCC - Confirm
        tccRestAdapter.confirmAll(participantLinks);

        log.info("End of place order");
    }

    private ParticipationRequest reduceStock(final OrderVO orderVO) {
        final String requestURL = "http://localhost:8081/api/v1/stocks";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("adjustmentType", "REDUCE");
        requestBody.put("productId", orderVO.getProductId());
        requestBody.put("qty", orderVO.getQty());

        return new ParticipationRequest(requestURL, requestBody);
    }

    private ParticipationRequest payOrder(final OrderVO orderVO) {
        final String requestURL = "http://localhost:8082/api/v1/payments";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderVO.getOrderId());
        requestBody.put("paymentAmt", orderVO.getPaymentAmt());

        return new ParticipationRequest(requestURL, requestBody);
    }

    private void waitCurrentThread(int seconds) throws InterruptedException {
        Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(seconds));
    }
}