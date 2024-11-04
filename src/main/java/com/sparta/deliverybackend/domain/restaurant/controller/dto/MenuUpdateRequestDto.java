package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Getter;

@Getter
public class MenuUpdateRequestDto {

	private String name;
	private Integer price;
	private String description;
	private String cuisineType;
}
