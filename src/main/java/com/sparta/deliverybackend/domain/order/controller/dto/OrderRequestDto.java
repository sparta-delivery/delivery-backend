package com.sparta.deliverybackend.domain.order.controller.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class OrderRequestDto {
	// 메뉴
	private List<OrderMenuDto> menus;

}
