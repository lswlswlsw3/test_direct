package com.sicc.order.vo;

import org.springframework.stereotype.Component;

/**
 * 주문 VO
 * @author Woongs
 */
public class OrderVO {
	private String orderId; // 주문 ID
    private String productId; // 제품 ID
    private Long qty; // 수량
    private Long paymentAmt; // 가격

    // setter & getter
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Long getQty() {
		return qty;
	}
	public void setQty(Long qty) {
		this.qty = qty;
	}
	public Long getPaymentAmt() {
		return paymentAmt;
	}
	public void setPaymentAmt(Long paymentAmt) {
		this.paymentAmt = paymentAmt;
	}
	
	// toString
	@Override
	public String toString() {
		return "OrderVO [orderId=" + orderId + ", productId=" + productId + ", qty=" + qty + ", paymentAmt="
				+ paymentAmt + "]";
	}
}