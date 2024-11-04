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
}
