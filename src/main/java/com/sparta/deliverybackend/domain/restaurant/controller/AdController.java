package com.sparta.deliverybackend.domain.restaurant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.AdRespDto;
import com.sparta.deliverybackend.domain.restaurant.service.AdService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ad")
public class AdController {

	private final AdService adService;

	/// 광고 생성
	@PostMapping()
	public ResponseEntity<AdRespDto> createAd(@RequestBody AdReqDto adReqDto, VerifiedMember verifiedMember){
		AdRespDto adRespDto = adService.createAd(adReqDto, verifiedMember);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(adRespDto);
	}

	// 광고 활성화
	@PatchMapping("/{adId}/active")
	public ResponseEntity<Void> activeAds(@PathVariable Long adId){
		adService.activeAds(adId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.build();
	}

	// 광고 비활성화 (삭제)
	@DeleteMapping("/{adId}/inactive")
	public ResponseEntity<Void> inActiveAds(@PathVariable Long adId){
		adService.inActiveAds(adId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
