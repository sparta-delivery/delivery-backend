package com.sparta.deliverybackend.domain.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantAddRespDto;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantDeleteRespDto;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantViewRespDto;
import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.FavoriteRestaurantRepository;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteRestaurantService {
	private final MemberRepository memberRepository;
	private final RestaurantRepository restaurantRepository;
	private final FavoriteRestaurantRepository favoriteRestaurantRepository;

	public FavoriteRestaurantAddRespDto addFavoriteRestaurant(Long restaurantId, VerifiedMember verifiedMember) {

		Member member = findMember(verifiedMember);
		Restaurant restaurant = findRestaurant(restaurantId);

		// 기존의 FavoriteRestaurant 존재 여부 확인
		Optional<FavoriteRestaurant> existingFavorite = favoriteRestaurantRepository
			.findByRestaurantAndMember(restaurant, member);

		FavoriteRestaurant favoriteRestaurant;

		if (existingFavorite.isPresent()) {
			favoriteRestaurant = existingFavorite.get();

			if (favoriteRestaurant.getDeletedAt() != null) {
				favoriteRestaurant.setDeletedAt(null);
			}
		} else {
			favoriteRestaurant = new FavoriteRestaurant(restaurant, member);
		}

		FavoriteRestaurant savedFavoriteRestaurant = favoriteRestaurantRepository.save(favoriteRestaurant);
		return new FavoriteRestaurantAddRespDto(savedFavoriteRestaurant);
	}

	@Transactional
	public FavoriteRestaurantDeleteRespDto deleteFavoriteRestaurant(Long restaurantId, VerifiedMember verifiedMember) {

		Member member = findMember(verifiedMember);
		Restaurant restaurant = findRestaurant(restaurantId);

		FavoriteRestaurant favoriteRestaurant = favoriteRestaurantRepository.findByRestaurantAndMember(restaurant,
				member)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_FAVORITE));

		favoriteRestaurant.delete();
		favoriteRestaurantRepository.save(favoriteRestaurant);
		return new FavoriteRestaurantDeleteRespDto(favoriteRestaurant);
	}

	public List<FavoriteRestaurantViewRespDto> getFavoriteRestaurants(VerifiedMember verifiedMember) {

		Member member = findMember(verifiedMember);

		List<FavoriteRestaurant> favoriteRestaurantList = favoriteRestaurantRepository
			.findAllByMemberAndDeletedAtIsNull(member);

		return favoriteRestaurantList.stream()
			.map(FavoriteRestaurantViewRespDto::new)
			.collect(Collectors.toList());
	}

	private Member findMember(VerifiedMember verifiedMember) {
		return memberRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
	}

	private Restaurant findRestaurant(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_RESTAURANT));
	}
}
