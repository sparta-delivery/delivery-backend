package com.sparta.deliverybackend.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

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
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuUpdateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.OptionReqDto;
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

	private VerifiedMember verifiedMember;
	private Restaurant restaurant;
	private Menu menu;
	private Manager manager;

	@BeforeEach
	void setUp() {
		verifiedMember = new VerifiedMember(1L);

		manager = Manager.builder()
			.id(1L)
			.nickname("Manager")
			.email("manager@manager.com")
			.build();

		restaurant = Restaurant.builder()
			.id(1L)
			.name("Sample restaurant")
			.build();

		menu = Menu.builder()
			.id(1L)
			.name("Sample menu")
			.price(20000)
			.description("Sample food")
			.cuisineType(CuisineType.KOREAN)
			// .cuisineType(CuisineType.JAPANESE)
			.restaurant(restaurant)
			.build();
	}


	@Test
	@DisplayName("메뉴 생성 테스트")
	void createMenuTest(){

		//given
		MenuCreateReqDto menuCreateReqDto = new MenuCreateReqDto("Sample menu", "KOREAN", 20000,"Sample food", 1L);

		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRepository.save(any(Menu.class))).thenReturn(menu);

		// when
		MenuRespDto menuRespDto = menuService.createMenu(menuCreateReqDto, verifiedMember);

		// then
		assertNotNull(menuRespDto);
		assertEquals("Sample menu", menuRespDto.getName());
		assertEquals(20000, menuRespDto.getPrice());
		assertEquals(CuisineType.KOREAN, menuRespDto.getCuisineType());
		verify(menuRepository, times(1)).save(any(Menu.class));
	}

	@Test
	@DisplayName("메뉴 업데이트 테스트")
	void updateMenuTest(){
		// given
		MenuUpdateReqDto menuUpdateReqDto = new MenuUpdateReqDto("Updated menu", 15000, "Updated menu", "KOREAN", List.of());

		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

		// when
		assertDoesNotThrow(() -> menuService.updateMenu(1L, 1L, menuUpdateReqDto, verifiedMember));

		// then
		verify(menuRepository, times(1)).save(any(Menu.class));
	}

	@Test
	@DisplayName("메뉴 삭제 테스트")
	void deleteMenuTest(){
		// given
		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

		// when
		assertDoesNotThrow(() -> menuService.deleteMenu(1L, 1L, verifiedMember));

		// then
		verify(menuRepository, times(1)).save(menu);
	}

	@Test
	@DisplayName("메뉴 옵션 조회 테스트")
	void getMenuOptionsTest() {
		// given
		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

		// when
		List<OptionReqDto> options = menuService.getMenuOptions(1L);

		// then
		assertNotNull(options);
		verify(menuRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("메뉴 옵션 업데이트 테스트")
	void updateMenuOptionsTest() {
		// given
		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

		// when
		assertDoesNotThrow(() -> menuService.updateMenuOptions(1L, 1L, List.of(), verifiedMember));

		// then
		verify(menuRepository, times(1)).save(menu);
	}
}
