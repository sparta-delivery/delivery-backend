package com.sparta.deliverybackend.domain.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
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
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdService {

	private final AdRepository adRepository;
	private final RestaurantRepository restaurantRepository;

	// 광고 조회 검증
	private Ad findAd(Long adId) {
		return adRepository.findById(adId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_AD));
	}

	@Transactional
	public AdRespDto createAd(AdReqDto adReqDto, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(adReqDto.getRestaurantId())
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_RESTAURANT));

		if (!restaurant.getManager().getId().equals(verifiedMember.id())) {
			throw new NotHaveAuthorityException(ExceptionCode.NOT_HAVE_AUTHORITY_AD_CREATE);
		}

		if(adRepository.existsById(restaurant.getId())){
			throw new IllegalArgumentException("이미 광고가 등록된 가게입니다. 다시 확인해 주세요.");
		}

		Ad ad = Ad.builder()
			.restaurant(restaurant)
			// 광고 최초 생성 시 광고 비활성화 상태
			.adStatus(AdStatus.INACTIVE)
			.build();

		adRepository.save(ad);
		return AdRespDto.from(ad);
	}

	@Transactional
	public void activeAds(Long adId) {
		Ad ad = findAd(adId);
		ad.activeAds();
	}

	@Transactional
	public void inActiveAds(Long adId) {
		Ad ad = findAd(adId);
		ad.inActiveAds();
	}

	@Transactional
	public void deleteAd(Long adId) {
		Ad ad = findAd(adId);
		ad.delete();
	}
}
