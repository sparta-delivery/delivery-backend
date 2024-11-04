package com.sparta.deliverybackend.domain.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuCreateRequestDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuResponseDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuUpdateRequestDto;
import com.sparta.deliverybackend.domain.restaurant.entity.CuisineType;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final RestaurantRepository restaurantRepository;
	private final MenuRepository menuRepository;

	@Transactional
	public MenuResponseDto createMenu(MenuCreateRequestDto menuCreateRequestDto) {
		Menu menu = menuRepository.save(Menu.from(menuCreateRequestDto));
		return menu.to();
	}

	@Transactional
	public void updateMenu(Long restaurantId, Long menuId, MenuUpdateRequestDto menuUpdateRequestDto) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

		if(!menu.getRestaurant().getId().equals(restaurant.getId())){
			throw new IllegalArgumentException("해당 가게의 메뉴가 아닙니다. 다시 확인해주세요");
		}

		menu.setName(menuUpdateRequestDto.getName());
		menu.setPrice(menuUpdateRequestDto.getPrice());
		menu.setDescription(menuUpdateRequestDto.getDescription());
		menu.setCuisineType(CuisineType.valueOf(menuUpdateRequestDto.getCuisineType()));

		menuRepository.save(menu);
	}

	@Transactional
	public void deleteMenu(Long restaurantId, Long menuId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

		if(!menu.getRestaurant().getId().equals(restaurant.getId())){
			throw new IllegalArgumentException("해당 가게의 메뉴가 아닙니다. 다시 확인해주세요");
		}

		// 메뉴 삭제 상태 변경 -> 엔티티에 deleted 메소드 추가
		menu.setDeleted(true);
		menuRepository.save(menu);
	}
}
