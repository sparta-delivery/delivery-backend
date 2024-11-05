package com.sparta.deliverybackend.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.CuisineType;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private ManagerRepository managerRepository;

	@Test
	@DisplayName("메뉴 생성 테스트")
	void createMenuTest(){

		//given
		Long managerId = 1L;
		VerifiedMember verifiedMember = new VerifiedMember(managerId);

		Manager manager = Manager.builder()
			.id(managerId)
			.nickname("Manager")
			.email("manager@manager.com")
			.build();

		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.name("Sample restaurant")
			.build();

		MenuCreateReqDto menuCreateReqDto = new MenuCreateReqDto("Sample menu", "KOREAN", 20000,"Sample food", 1L);

		Menu menu = Menu.builder()
			.id(1L)
			.name("Sample menu")
			.price(20000)
			.description("Sample food")
			.cuisineType(CuisineType.KOREAN)
			.restaurant(restaurant)
			.build();

		when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
		when(restaurantRepository.findById(menuCreateReqDto.getRestaurantId())).thenReturn(Optional.of(restaurant));
		when(menuRepository.save(any(Menu.class))).thenReturn(menu);

		// when
		MenuRespDto menuRespDto = menuService.createMenu(menuCreateReqDto, verifiedMember);

		// then
		assertNotNull(menuRespDto);
		assertEquals("Sample menu", menuRespDto.getName());
		assertEquals(20000, menuRespDto.getPrice());
		assertEquals(CuisineType.KOREAN, menuRespDto.getCuisineType());
	}

	@Test
	@DisplayName("")
}
