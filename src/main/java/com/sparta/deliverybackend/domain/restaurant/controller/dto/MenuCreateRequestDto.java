package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuCreateRequestDto {

	private String name;
	private String cuisineType;
	private Integer price;
	private String description;
	private Long restaurantId;
}
