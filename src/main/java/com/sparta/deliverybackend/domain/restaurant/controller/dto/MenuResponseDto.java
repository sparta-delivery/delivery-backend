package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import java.time.LocalDateTime;

import com.sparta.deliverybackend.domain.restaurant.entity.CuisineType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuResponseDto {

	private Long id;
	private String name;
	private Integer price;
	private String description;
	private CuisineType cuisineType;
	private Long restaurantId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
