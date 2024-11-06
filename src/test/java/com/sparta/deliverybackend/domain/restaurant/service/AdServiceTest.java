package com.sparta.deliverybackend.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Ad;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.AdRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AdServiceTest {

	@InjectMocks
	private AdService adService;

	@Mock
	private AdRepository adRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private ManagerRepository managerRepository;

	private VerifiedMember verifiedMember;
	private Manager manager;
	private Restaurant restaurant;
	private Ad ad;

	@BeforeEach
	void setUp() {
		verifiedMember = new VerifiedMember(1L);

		manager = Manager.builder()
			.id(1L)
			.nickname("manager")
			.email("manager@example.com")
			.build();

		restaurant = Restaurant.builder()
			.id(1L)
			.manager(manager)
			.build();

		ad = Ad.builder()
			.id(1L)
			.restaurant(restaurant)
			.isActive(false)
			.build();
	}

	@Test
	@DisplayName("광고 생성 테스트")
	void createAdTest() {
		// given
		AdReqDto adReqDto = new AdReqDto(1L);
		when(restaurantRepository.findById(adReqDto.getRestaurantId())).thenReturn(Optional.of(restaurant));
		when(managerRepository.findById(verifiedMember.id())).thenReturn(Optional.of(manager));
		when(adRepository.save(any(Ad.class))).thenReturn(ad);

		// when
		AdRespDto result = adService.createAd(adReqDto, verifiedMember);

		// then
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertFalse(result.isActive());
		verify(adRepository, times(1)).save(any(Ad.class));
	}

	@Test
	@DisplayName("광고 활성화 테스트")
	void activeAdsTest() {
		// given
		when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

		// when
		adService.activeAds(1L);

		// then
		assertTrue(ad.isActive());
		verify(adRepository, times(1)).save(ad);
	}

	@Test
	@DisplayName("광고 비활성화 테스트")
	void inActiveAdsTest() {
		// given
		when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

		// when
		adService.inActiveAds(1L);

		// then
		assertFalse(ad.isActive());
		verify(adRepository, times(1)).save(ad);
	}

	@Test
	@DisplayName("광고를 찾을 수 없는 경우 테스트")
	void findAdOrThrowNotFoundTest() {
		// given
		when(adRepository.findById(2L)).thenReturn(Optional.empty());

		// when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			adService.activeAds(2L);
		});

		// then
		assertEquals("광고를 찾을 수 없습니다.", exception.getMessage());
	}
}
