package com.sparta.deliverybackend.domain.member.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteRestaurantService {
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    public FavoriteRestaurantAddRespDto addFavoriteRestaurant(Long restaurantId, VerifiedMember verifiedMember) {
        // 멤버와 가게 확인
        Member member = memberRepository.findById(verifiedMember.id())
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 가게가 존재하지 않습니다."));

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

        // 멤버 확인
        Member member = memberRepository.findById(verifiedMember.id())
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 가게가 존재하지 않습니다."));

        FavoriteRestaurant favoriteRestaurant = favoriteRestaurantRepository.findByRestaurantAndMember(restaurant, member)
                .orElseThrow(() -> new IllegalArgumentException("해당 즐겨찾기가 존재하지 않습니다."));

        favoriteRestaurant.delete();
        favoriteRestaurantRepository.save(favoriteRestaurant);
        return new FavoriteRestaurantDeleteRespDto(favoriteRestaurant);
    }

    public List<FavoriteRestaurantViewRespDto> getFavoriteRestaurants(VerifiedMember verifiedMember) {

        Member member = memberRepository.findById(verifiedMember.id())
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));

        List<FavoriteRestaurant> favoriteRestaurantList = favoriteRestaurantRepository
                .findAllByMemberAndDeletedAtIsNull(member);

        return favoriteRestaurantList.stream()
                .map(FavoriteRestaurantViewRespDto::new)
                .collect(Collectors.toList());
    }
}
