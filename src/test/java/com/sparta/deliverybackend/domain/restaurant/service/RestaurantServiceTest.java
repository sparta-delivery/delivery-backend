package com.sparta.deliverybackend.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantDeleteRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantUpdateRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantUpdateReqDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;

class RestaurantServiceTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private ManagerRepository managerRepository;

	@InjectMocks
	private RestaurantService restaurantService;
	private VerifiedMember verifiedMember;
	private Manager manager;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		verifiedMember = new VerifiedMember(1L);
		manager = new Manager();
		manager.setId(1L);
	}

	@Test
	@DisplayName("가게 생성 테스트")
	void testCreateRestaurant() {
		RestaurantCreateReqDto reqDto = new RestaurantCreateReqDto("Test Restaurant", "09:00", "22:00", 1000);
		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
		when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

		RestaurantCreateRepDto result = restaurantService.createRestaurant(reqDto, verifiedMember);

		assertNotNull(result);
		assertEquals("Test Restaurant", result.getName());
		verify(restaurantRepository).save(any(Restaurant.class));
	}

	@Test
	@DisplayName("가게 다건 조회 테스트")
	void testGetRestaurants() {
		Pageable pageable = PageRequest.of(0, 10);

		Restaurant restaurant1 = new Restaurant("Test Restaurant 1", "09:00", "22:00", 1000, manager);
		Restaurant restaurant2 = new Restaurant("Test Restaurant 2", "10:00", "23:00", 2000, manager);
		Restaurant restaurant3 = new Restaurant("Test Restaurant 3", "11:00", "21:00", 1500, manager);

		List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2, restaurant3);
		Page<Restaurant> restaurantPage = new PageImpl<>(restaurants, pageable, restaurants.size());

		when(restaurantRepository.findAll(pageable)).thenReturn(restaurantPage);

		Page<RestaurantCreateRepDto> result = restaurantService.getRestaurants(pageable, verifiedMember);

		assertEquals(3, result.getTotalElements()); // 총 요소 개수 검증
		assertEquals("Test Restaurant 1", result.getContent().get(0).getName()); // 첫 번째 레스토랑 이름 검증
		assertEquals("Test Restaurant 2", result.getContent().get(1).getName()); // 두 번째 레스토랑 이름 검증
		assertEquals("Test Restaurant 3", result.getContent().get(2).getName()); // 세 번째 레스토랑 이름 검증
	}

	@Test
	@DisplayName("가게 수정 테스트")
	void testUpdateRestaurant() {
		Long restaurantId = 1L;
		RestaurantUpdateReqDto reqDto = new RestaurantUpdateReqDto("Updated Restaurant", "10:00", "21:00", 1500);
		Restaurant restaurant = new Restaurant("Test Restaurant", "09:00", "22:00", 1000, manager);
		restaurant.setId(restaurantId);

		when(managerRepository.findById(verifiedMember.id())).thenReturn(Optional.of(manager));
		when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

		RestaurantUpdateRepDto result = restaurantService.updateRestaurant(restaurantId, reqDto, verifiedMember);
		assertNotNull(result);
		assertEquals(restaurantId, result.getId()); // ID 비교
		verify(restaurantRepository).findById(restaurantId);

	}

	@Test
	@DisplayName("가게 탈퇴 테스트")
	void testDeleteRestaurant() {
		Long restaurantId = 1L;
		Restaurant restaurant = new Restaurant("Test Restaurant", "09:00", "22:00", 1000, manager);

		when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

		RestaurantDeleteRepDto result = restaurantService.deleteRestaurant(restaurantId, verifiedMember);

		assertNotNull(result);
		// 현재 시간과 deletedAt이 1초 이내에 있는지 확인
		LocalDateTime now = LocalDateTime.now();
		assertTrue(
			result.getDeletedAt().isAfter(now.minusSeconds(1)) && result.getDeletedAt().isBefore(now.plusSeconds(1)),
			"DeletedAt should be within 1 second of the current time");

		verify(restaurantRepository).save(any(Restaurant.class));
	}

}