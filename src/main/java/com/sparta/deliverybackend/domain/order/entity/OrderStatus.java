package com.sparta.deliverybackend.domain.order.entity;

public enum OrderStatus {

	WAIT("WAIT"),
	COOKING("COOKING"),
	DELIVERING("DELIVERING"),
	COMPLETE("COMPLETE");

	private String OrderStatusValue;

	OrderStatus(String orderStatusValue) {
		OrderStatusValue = orderStatusValue;
	}
}
