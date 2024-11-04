package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuUpdateRequestDto {

	@NotBlank(message = "이름을 입력해 주세요.")
	private String name;

	@NotNull(message = "가격을 입력해 주세요.")
	@Min(value = 1000, message = "가격은 최소 1000원 이상이어야 합니다.")
	private Integer price;

	@NotBlank(message = "음식의 설명을 입력해 주세요.")
	private String description;

	@NotBlank(message = "음식 카테고리를 선택해 주세요.")
	private String cuisineType;
}
