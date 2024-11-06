package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class AdReqDto {

	private Long restaurantId;

	public AdReqDto(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}
