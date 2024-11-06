package com.sparta.deliverybackend.domain.restaurant.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdService {

	private final AdRepository adRepository;
	private final RestaurantRepository restaurantRepository;
	private final ManagerRepository managerRepository;

	// 광고 조회 검증
	private Ad findAdOrThrow(Long adId){
		return adRepository.findById(adId)
			.orElseThrow(() -> new EntityNotFoundException("광고를 찾을 수 없습니다."));
	}

	@Transactional
	public AdRespDto createAd(AdReqDto adReqDto, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(adReqDto.getRestaurantId())
			.orElseThrow(()-> new EntityNotFoundException("가게를 찾을 수 없습니다."));

		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new IllegalArgumentException("광고 생성 권한이 없습니다."));

		if(!restaurant.getManager().getId().equals(manager.getId())){
			throw new IllegalArgumentException("사장님 가게가 아닙니다. 광고를 생성할 권한이 없습니다.");
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
