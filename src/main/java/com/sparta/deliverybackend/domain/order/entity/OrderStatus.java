package com.sparta.deliverybackend.domain.order.entity;

public enum OrderStatus {

	WAIT("WAIT"),
	COOKING("COOKING"),
	DELIVERING("DELIVERING"),
	COMPLETE("COMPLETE"),
	CANCELED("CANCELED");

	private String OrderStatusValue;

	OrderStatus(String orderStatusValue) {
		OrderStatusValue = orderStatusValue;
	}

	public OrderStatus next() {
		return switch (this) {
			case WAIT -> COOKING;
			case COOKING -> DELIVERING;
			case DELIVERING, COMPLETE -> COMPLETE;
			case CANCELED -> CANCELED;
		};
	}

}
