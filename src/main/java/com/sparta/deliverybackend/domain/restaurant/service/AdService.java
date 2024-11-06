package com.sparta.deliverybackend.domain.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Ad;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.AdRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.customException.NotHaveAuthorityException;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdService {

	private final AdRepository adRepository;
	private final RestaurantRepository restaurantRepository;
	private final ManagerRepository managerRepository;

	// 광고 조회 검증
	private Ad findAdOrThrow(Long adId) {
		return adRepository.findById(adId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_AD));
	}

	@Transactional
	public AdRespDto createAd(AdReqDto adReqDto, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(adReqDto.getRestaurantId())
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_RESTAURANT));

		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new NotHaveAuthorityException(ExceptionCode.NOT_HAVE_AUTHORITY_AD_CREATE));

		if (!restaurant.getManager().getId().equals(manager.getId())) {
			throw new NotHaveAuthorityException(ExceptionCode.NOT_HAVE_AUTHORITY_AD_CREATE);
		}

		Ad ad = Ad.builder()
			.restaurant(restaurant)
			.isActive(false)
			.build();

		adRepository.save(ad);
		return AdRespDto.from(ad);
	}

	@Transactional
	public void activeAds(Long adId) {
		Ad ad = findAdOrThrow(adId);

		ad.activeAds();
		adRepository.save(ad);
	}

	@Transactional
	public void inActiveAds(Long adId) {
		Ad ad = findAdOrThrow(adId);

		ad.inActiveAds();
		adRepository.save(ad);
	}
}
