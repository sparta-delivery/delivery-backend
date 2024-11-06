package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.Ad;
import com.sparta.deliverybackend.domain.restaurant.entity.AdStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdRespDto {

	private Long id;
	private Long restaurantId;
	private AdStatus adStatus;


	public static AdRespDto from(Ad ad) {
		return AdRespDto.builder()
			.id(ad.getId())
			.restaurantId(ad.getRestaurant().getId())
			.adStatus(ad.getAdStatus())
			.build();
	}

}
