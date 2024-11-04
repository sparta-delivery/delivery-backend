package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.CuisineType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponseDto {

	private Long id;
	private String name;
	private String description;
	private Integer price;
	private CuisineType cuisineType;
	private Long restaurantId;
}
