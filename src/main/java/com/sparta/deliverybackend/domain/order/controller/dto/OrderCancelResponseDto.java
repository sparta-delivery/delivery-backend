package com.sparta.deliverybackend.domain.order.controller.dto;

import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCancelResponseDto {

	private Long orderId;

	private OrderStatus status;

	public static OrderCancelResponseDto from(Order order) {
		return OrderCancelResponseDto.builder()
			.orderId(order.getId())
			.status(order.getOrderStatus())
			.build();
	}
}
