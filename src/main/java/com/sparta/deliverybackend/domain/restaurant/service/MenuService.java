package com.sparta.deliverybackend.domain.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuUpdateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.OptionReqDto;
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
	private final ManagerRepository managerRepository;

	@Transactional
	public MenuRespDto createMenu(MenuCreateReqDto menuCreateReqDto, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(menuCreateReqDto.getRestaurantId())
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 생성할 권한이 없습니다."));

		Menu menu = menuRepository.save(Menu.from(menuCreateReqDto, restaurant, manager));
		return menu.to();
	}

	@Transactional
	public void updateMenu(Long restaurantId, Long menuId, MenuUpdateReqDto menuUpdateReqDto,
		VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 생성할 권한이 없습니다."));

		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

		if(!menu.getRestaurant().getId().equals(restaurant.getId())){
			throw new IllegalArgumentException("해당 가게의 메뉴가 아닙니다. 다시 확인해주세요");
		}

		menu.isUpdated(menuUpdateReqDto.getName(), menuUpdateReqDto.getPrice(), menuUpdateReqDto.getDescription(),
			CuisineType.valueOf(menuUpdateReqDto.getCuisineType()));

		menuRepository.save(menu);
	}

	@Transactional
	public void deleteMenu(Long restaurantId, Long menuId, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 생성할 권한이 없습니다."));

		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

		if(!menu.getRestaurant().getId().equals(restaurant.getId())){
			throw new IllegalArgumentException("해당 가게의 메뉴가 아닙니다. 다시 확인해주세요");
		}

		menu.isDeleted();
		menuRepository.save(menu);
	}

	@Transactional(readOnly = true)
	public List<OptionReqDto> getMenuOptions(Long menuId){
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

		try{
			return menu.getOptions();
		} catch (JsonProcessingException e){
			throw new RuntimeException("옵션 조회 중 오류가 발생했습니다.", e);
		}
	}

	@Transactional
	public void updateMenuOptions(Long restaurantId, Long menuId, List<OptionReqDto> optionReqDtos, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 생성할 권한이 없습니다."));

		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

		if(!menu.getRestaurant().getId().equals(restaurant.getId())){
			throw new IllegalArgumentException("해당 가게의 메뉴가 아닙니다. 다시 확인해주세요");
		}

		menu.updateOptions(optionReqDtos);

		menuRepository.save(menu);
	}
}
