package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuCreateReqDto {

	@NotBlank(message = "음식명을 입력해 주세요.")
	private String name;

	@NotBlank(message = "음식 카테고리를 선택해 주세요.")
	private String cuisineType;

	@NotNull(message = "가격을 입력해 주세요.")
	@Min(value = 1000, message = "가격은 최소 1000원 이상이어야 합니다.")
	private Integer price;

	@NotBlank(message = "음식의 설명을 입력해 주세요.")
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
