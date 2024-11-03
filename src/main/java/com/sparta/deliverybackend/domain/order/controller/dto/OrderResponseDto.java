package com.sparta.deliverybackend.domain.order.controller.dto;

import java.util.List;

import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderMenu;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponseDto {

	// 주문한 사람의 이름 (Member 정보)
	private String customer;

	// 시킨 식당 이름
	private String restaurantName;

	// 시킨 메뉴 정보 (메뉴명 , 수량)
	private List<OrderMenuDto> requestMenus;

	// 총액
	private Long totalPrice;

	// 주문 상황
	private OrderStatus orderStatus;

	public static OrderResponseDto of(Order order, List<OrderMenu> orderMenus) {

		// OrderMenuDto 리스트 생성
		List<OrderMenuDto> deliveryMenus = orderMenus.stream()
			.map(menu -> OrderMenuDto.builder()
				.menu(menu.getMenu())
				.quantity(menu.getQuantity())
				.build())
			.toList();

		// 총액 계산
		long totalPrice = deliveryMenus.stream()
			.mapToLong(menu -> menu.getMenu().getPrice() * menu.getQuantity())
			.sum();

		return OrderResponseDto.builder()
			.customer(order.getMember().getNickname())
			.restaurantName(deliveryMenus.get(0).getMenu().getRestaurant().getName())
			.requestMenus(deliveryMenus)
			.totalPrice(totalPrice)
			.orderStatus(order.getOrderStatus())
			.build();

	}

}
