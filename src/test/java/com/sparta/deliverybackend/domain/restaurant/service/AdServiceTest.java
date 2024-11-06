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

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Ad;
import com.sparta.deliverybackend.domain.restaurant.entity.AdStatus;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.AdRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.customException.NotHaveAuthorityException;

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
	private Restaurant restaurant;
	private Manager manager;
	private Ad ad;

	@BeforeEach
	void setUp() {
		// given
		verifiedMember = new VerifiedMember(1L);
		manager = Manager.builder()
			.id(1L)
			.nickname("manager")
			.email("manager@manager.com")
			.build();

		restaurant = Restaurant.builder()
			.id(1L)
			.manager(manager)
			.build();

		ad = Ad.builder()
			.id(1L)
			.restaurant(restaurant)
			.adStatus(AdStatus.INACTIVE)
			.build();
	}

	@Test
	@DisplayName("광고 생성 테스트")
	void createAdTest() {
		// given
		AdReqDto adReqDto = new AdReqDto(restaurant.getId());

		when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
		when(managerRepository.findById(verifiedMember.id())).thenReturn(Optional.of(manager));
		when(adRepository.existsById(restaurant.getId())).thenReturn(false);
		when(adRepository.save(any(Ad.class))).thenReturn(ad);

		// when
		AdRespDto result = adService.createAd(adReqDto, verifiedMember);

		// then
		assertNotNull(result);
		assertEquals(AdStatus.INACTIVE, result.getAdStatus());
		verify(adRepository, times(1)).save(any(Ad.class));
	}

	@Test
	@DisplayName("광고 활성화 테스트")
	void activeAdsTest() {
		// given
		when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));

		// when
		adService.activeAds(ad.getId());

		// then
		assertEquals(AdStatus.ACTIVE, ad.getAdStatus());
		verify(adRepository, times(1)).save(ad);
	}

	@Test
	@DisplayName("광고 비활성화 테스트")
	void inActiveAdsTest() {
		// given
		when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));

		// when
		adService.inActiveAds(ad.getId());

		// then
		assertEquals(AdStatus.INACTIVE, ad.getAdStatus());
		verify(adRepository, times(1)).save(ad);
	}

	@Test
	@DisplayName("광고 삭제 테스트")
	void deleteAdTest() {
		// given
		when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));

		// when
		adService.deleteAd(ad.getId());

		// then
		assertEquals(AdStatus.DELETED, ad.getAdStatus());
		verify(adRepository, times(1)).save(ad);
	}

	@Test
	@DisplayName("광고 조회 실패 테스트")
	void findAdOrThrowNotFoundTest() {
		// given
		Long nonExistentAdId = 2L;
		when(adRepository.findById(nonExistentAdId)).thenReturn(Optional.empty());

		// when & then
		NotFoundEntityException exception = assertThrows(NotFoundEntityException.class, () -> {
			adService.activeAds(nonExistentAdId);
		});

		assertEquals("해당 광고를 찾을 수 없습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("광고 생성 권한 없을 때 테스트")
	void createAdWithoutAuthorityTest() {
		// given
		Manager otherManager = Manager.builder()
			.id(2L)
			.nickname("otherManager")
			.email("other@manager.com")
			.build();

		restaurant.setManager(otherManager);
		AdReqDto adReqDto = new AdReqDto(restaurant.getId());

		when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
		when(managerRepository.findById(verifiedMember.id())).thenReturn(Optional.of(manager));

		// when & then
		NotHaveAuthorityException exception = assertThrows(NotHaveAuthorityException.class, () -> {
			adService.createAd(adReqDto, verifiedMember);
		});

		assertEquals("광고 생성에 대한 권한이 없습니다.", exception.getMessage());
	}
}
