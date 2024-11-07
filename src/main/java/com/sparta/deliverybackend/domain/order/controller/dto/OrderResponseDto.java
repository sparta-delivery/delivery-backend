package com.sparta.deliverybackend.domain.order.controller.dto;

import java.time.LocalDateTime;
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

	// 주문 일시
	private LocalDateTime orderDate;

	public static OrderResponseDto of(Order order, List<OrderMenu> orderMenus) {

		Long totalPrice = orderMenus.stream()
			.map(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
			.reduce(0L, Long::sum);

		List<OrderMenuDto> requestMenus = orderMenus.stream()
			.map(orderMenu -> OrderMenuDto.builder()
				.menuId(orderMenu.getMenu().getId())
				.quantity(orderMenu.getQuantity())
				.build())
			.toList();

		return OrderResponseDto.builder()
			.customer(order.getMember().getNickname())
			.restaurantName(orderMenus.get(0).getMenu().getRestaurant().getName())
			.requestMenus(requestMenus)
			.totalPrice(totalPrice)
			.orderStatus(order.getOrderStatus())
			.orderDate(order.getCreatedAt())
			.build();
	}

}
