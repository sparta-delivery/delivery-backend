package com.sparta.deliverybackend.domain.order.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderMenuDto {
	private Menu menu;
	private Long quantity;
}
