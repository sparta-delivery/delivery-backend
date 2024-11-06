package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import java.time.LocalDateTime;

import com.sparta.deliverybackend.domain.restaurant.entity.Ad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdRespDto {

	private Long id;
	private Long restaurantId;
	private boolean isActive;

	public static AdRespDto from(Ad ad) {
		return AdRespDto.builder()
			.id(ad.getId())
			.restaurantId(ad.getRestaurant().getId())
			.isActive(ad.isActive())
			.build();
	}

}
