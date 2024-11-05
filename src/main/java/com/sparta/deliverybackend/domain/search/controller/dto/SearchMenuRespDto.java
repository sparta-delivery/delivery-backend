package com.sparta.deliverybackend.domain.search.controller.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchMenuRespDto {
	private Long menuId;
	private String menuName;
	private Integer price;
	private String menuDescription;
	private String restaurantName;
	private String restaurantOpen;
	private String restaurantClose;

	public static List<SearchMenuRespDto> from(Page<Menu> result) {
		return result.getContent().stream()
			.map(menu -> SearchMenuRespDto.builder()
				.menuId(menu.getId())
				.menuName(menu.getName())
				.price(menu.getPrice())
				.menuDescription(menu.getDescription())
				.restaurantName(menu.getRestaurant().getName())
				.restaurantOpen(menu.getRestaurant().getOpenTime())
				.restaurantClose(menu.getRestaurant().getCloseTime())
				.build())
			.toList();
	}
}
