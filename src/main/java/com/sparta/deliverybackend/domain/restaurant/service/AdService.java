package com.sparta.deliverybackend.domain.restaurant.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Ad;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.AdRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdService {

	private final AdRepository adRepository;
	private final RestaurantRepository restaurantRepository;

	@Transactional
	public AdRespDto createAd(AdReqDto adReqDto, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(adReqDto.getRestaurantId())
			.orElseThrow(()-> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Ad ad = Ad.builder()
			.restaurant(restaurant)
			.isActive(false)
			.build();

		adRepository.save(ad);
		return AdRespDto.from(ad);
	}

	@Transactional
	public void activeAds(Long adId) {
		Ad ad = adRepository.findById(adId)
			.orElseThrow(() -> new EntityNotFoundException("광고를 찾을 수 없습니다."));

		ad.activeAds();
		adRepository.save(ad);
	}

	@Transactional
	public void inActiveAds(Long adId) {
		Ad ad = adRepository.findById(adId)
			.orElseThrow(() -> new EntityNotFoundException("광고를 찾을 수 없습니다."));

		ad.inActiveAds();
		adRepository.save(ad);
	}
}
