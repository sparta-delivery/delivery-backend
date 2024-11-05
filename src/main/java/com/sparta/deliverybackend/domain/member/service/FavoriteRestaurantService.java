package com.sparta.deliverybackend.domain.member.service;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantAddRespDto;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantDeleteRespDto;
import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.FavoriteRestaurantRepository;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteRestaurantService {
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    public FavoriteRestaurantAddRespDto addFavoriteRestaurant(Long restaurantId, VerifiedMember verifiedMember) {
        //멤버랑 가게 존재
        Member memberId = memberRepository.findById(verifiedMember.id())
                .orElseThrow(()-> new IllegalArgumentException("멤버가 존재하지 않습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new IllegalArgumentException("선택한 가게가 존재하지 않습니다"));

        FavoriteRestaurant favoriteRestaurant = new FavoriteRestaurant(restaurant, memberId);
        FavoriteRestaurant savedFavoriteRestaurant = favoriteRestaurantRepository.save(favoriteRestaurant);

        return new FavoriteRestaurantAddRespDto(savedFavoriteRestaurant);
    }

    @Transactional
    public FavoriteRestaurantDeleteRespDto deleteFavoriteRestaurant(Long restaurantId, VerifiedMember verifiedMember) {
//        //멤버랑 가게 존재
//        Member memberId = memberRepository.findById(verifiedMember.id())
//                .orElseThrow(()-> new IllegalArgumentException("멤버가 존재하지 않습니다."));
//
//        Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                .orElseThrow(()-> new IllegalArgumentException("선택한 가게가 존재하지 않습니다"));
//
        FavoriteRestaurant favoriteRestaurant = favoriteRestaurantRepository.findByRestaurantId(restaurantId);

        favoriteRestaurant.delete();
        favoriteRestaurantRepository.save(favoriteRestaurant);
        return new FavoriteRestaurantDeleteRespDto(favoriteRestaurant);
    }
}
