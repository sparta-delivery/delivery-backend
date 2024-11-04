package com.sparta.deliverybackend.domain.restaurant.service;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;

    public RestaurantCreateRepDto createRestaurant(RestaurantCreateReqDto reqDto, VerifiedMember verifiedMember) {//로그인 유저로 바꿀 예정
        Manager manager = managerRepository.findById(reqDto.getManagerId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사장님 아이디입니다."));

        Restaurant restaurant = new Restaurant(reqDto.getName(), reqDto.getOpenTime(), reqDto.getCloseTime(), reqDto.getMinPrice(), manager);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return new RestaurantCreateRepDto(savedRestaurant);
    }

//    public List<RestaurantCreateRepDto> getRestaurants(VerifiedMember verifiedMember) {
//
//        return
//    }
}
