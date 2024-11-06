package com.sparta.deliverybackend.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sparta.deliverybackend.api.s3.service.S3Service;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.*;
import com.sparta.deliverybackend.domain.restaurant.entity.CuisineType;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
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
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class RestaurantServiceTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private ManagerRepository managerRepository;

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private RestaurantService restaurantService;
	private VerifiedMember verifiedMember;
	private Manager manager;

	@Mock
	private S3Service s3Service;

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
		// Given
		RestaurantCreateReqDto reqDto = new RestaurantCreateReqDto("Test Restaurant", "09:00", "22:00", 1000);

		// Mocking MultipartFile
		MultipartFile profileImg = new MockMultipartFile("file", "filename.jpg", "image/jpeg", "image content".getBytes());

		// Mock S3Service의 uploadImage 메서드
		String imageUrl = "https://your-bucket-name.s3.amazonaws.com/uploads/filename.jpg"; // 원하는 URL 설정
		when(s3Service.uploadImage(profileImg)).thenReturn(imageUrl);

		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
		when(restaurantRepository.countByManagerId(manager.getId())).thenReturn(2L); // 2개의 가게가 이미 존재

		// Restaurant 객체를 생성할 때 imageUrl을 포함
		Restaurant restaurant = new Restaurant(reqDto.getName(), reqDto.getOpenTime(), reqDto.getCloseTime(), reqDto.getMinPrice(), manager);

		when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> {
			Restaurant savedRestaurant = invocation.getArgument(0);
			return savedRestaurant;
		});

		RestaurantCreateRespDto result = restaurantService.createRestaurant(reqDto, verifiedMember, profileImg);

		assertNotNull(result);
		assertEquals("Test Restaurant", result.getName());
		verify(restaurantRepository).save(any(Restaurant.class));
	}


	@Test
	@DisplayName("가게 다건 조회 테스트")
	void testGetRestaurants() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);

		Restaurant restaurant1 = new Restaurant("Test Restaurant 1", "09:00", "22:00", 1000, manager);
		Restaurant restaurant2 = new Restaurant("Test Restaurant 2", "10:00", "23:00", 2000, manager);
		Restaurant restaurant3 = new Restaurant("Test Restaurant 3", "11:00", "21:00", 1500, manager);

		List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2, restaurant3);
		Page<Restaurant> restaurantPage = new PageImpl<>(restaurants, pageable, restaurants.size());

		// Mocking the repository method to return the restaurant page
		when(restaurantRepository.findAllByDeletedAtIsNull(pageable)).thenReturn(restaurantPage);

		// When
		Page<RestaurantCreateRespDto> result = restaurantService.getRestaurants(pageable, verifiedMember);

		// Then
		assertEquals(3, result.getTotalElements()); // 총 요소 개수 검증
		assertEquals("Test Restaurant 1", result.getContent().get(0).getName()); // 첫 번째 레스토랑 이름 검증
		assertEquals("Test Restaurant 2", result.getContent().get(1).getName()); // 두 번째 레스토랑 이름 검증
		assertEquals("Test Restaurant 3", result.getContent().get(2).getName()); // 세 번째 레스토랑 이름 검증
	}

	@Test
	@DisplayName("가게 단건 조회 테스트")
	void testGetRestaurantInfo(){

		Long restaurantId = 1L;
		Manager manager = new Manager();
		manager.setId(1L); // Manager의 ID 설정

		Restaurant restaurant = new Restaurant("Test Restaurant", "09:00", "22:00", 1000, manager);
		restaurant.setId(restaurantId);

		Menu menu1 = new Menu(1L, "Menu1", 1500, "Description1", CuisineType.KOREAN, restaurant, null, "option");
		Menu menu2 = new Menu(2L, "Menu2", 2000, "Description2", CuisineType.CHINESE, restaurant, null, "option");

		List<Menu> menus = Arrays.asList(menu1, menu2);

		when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
		when(menuRepository.findByRestaurantId(restaurantId)).thenReturn(menus);
		when(managerRepository.findById(verifiedMember.id())).thenReturn(Optional.of(manager));

		RestaurantViewRespDto response = restaurantService.getRestaurantInfo(restaurantId, verifiedMember);

		assertNotNull(response); // 응답이 null이 아니어야 함
		assertEquals(2, response.getMenus().size()); // 메뉴 리스트 사이즈 검증
		assertEquals("Menu1", response.getMenus().get(0).getName()); // 첫 번째 메뉴 이름 검증
		assertEquals("Menu2", response.getMenus().get(1).getName()); // 두 번째 메뉴 이름 검증
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

		RestaurantUpdateRespDto result = restaurantService.updateRestaurant(restaurantId, reqDto, verifiedMember);
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

		RestaurantDeleteRespDto result = restaurantService.deleteRestaurant(restaurantId, verifiedMember);

		assertNotNull(result);
		// 현재 시간과 deletedAt이 1초 이내에 있는지 확인
		LocalDateTime now = LocalDateTime.now();
		assertTrue(
			result.getDeletedAt().isAfter(now.minusSeconds(1)) && result.getDeletedAt().isBefore(now.plusSeconds(1)),
			"DeletedAt should be within 1 second of the current time");

		verify(restaurantRepository).save(any(Restaurant.class));
	}

}