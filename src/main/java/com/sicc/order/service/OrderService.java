package com.sicc.order.service;

import com.sicc.order.vo.OrderVO;

/**
 * 주문 service 정의
 * @author Woongs
 */
public interface OrderService {
	void placeOrder(OrderVO orderVO); // 주문
}
