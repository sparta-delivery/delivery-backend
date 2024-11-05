package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuCreateReqDto {

	private String name;
	private String cuisineType;
	private Integer price;
	private String description;
	private Long restaurantId;

	public MenuCreateReqDto(String name, String cuisineType, Integer price, String description, Long restaurantId) {
		this.name = name;
		this.cuisineType = cuisineType;
		this.price = price;
		this.description = description;
		this.restaurantId = restaurantId;
	}
}
