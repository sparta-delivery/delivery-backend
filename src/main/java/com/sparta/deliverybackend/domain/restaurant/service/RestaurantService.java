package com.sparta.deliverybackend.domain.restaurant.service;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;

    public RestaurantCreateRepDto createRestaurant(RestaurantCreateReqDto reqDto, VerifiedMember verifiedMember) {//로그인 유저로 바꿀 예정
        Manager manager = managerRepository.findById(verifiedMember.id())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사장님 아이디입니다."));

        Restaurant restaurant = new Restaurant(reqDto.getName(), reqDto.getOpenTime(), reqDto.getCloseTime(), reqDto.getMinPrice(), manager);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return new RestaurantCreateRepDto(savedRestaurant);
    }

    public List<RestaurantCreateRepDto> getRestaurants(VerifiedMember verifiedMember) {
        List<RestaurantCreateRepDto> restaurants = restaurantRepository.findAll().stream().map(RestaurantCreateRepDto::new).toList();
        return restaurants;
    }

//    public List<RestaurantCreateRepDto> getRestaurants(int page, int limit, VerifiedMember verifiedMember) {
//        PageRequest pageRequest = PageRequest.of(page, limit);
//        Page<Restaurant> restaurants = restaurantRepository.findAll(pageRequest);
//
//    }
}
