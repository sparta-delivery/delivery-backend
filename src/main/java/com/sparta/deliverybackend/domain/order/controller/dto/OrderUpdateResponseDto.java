package com.sparta.deliverybackend.domain.order.controller.dto;

import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderUpdateResponseDto {

	private Long orderId;

	private OrderStatus status;

	public static OrderUpdateResponseDto from(Order order) {
		return OrderUpdateResponseDto.builder()
			.orderId(order.getId())
			.status(order.getOrderStatus())
			.build();
	}
}
