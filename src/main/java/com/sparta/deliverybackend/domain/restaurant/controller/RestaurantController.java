package com.sparta.deliverybackend.domain.restaurant.controller;

import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurant")
    public RestaurantCreateRepDto createRestaurant(@RequestBody RestaurantCreateReqDto reqDto, Manager managerId){//로그인 유저로 바꿀 예정
        return restaurantService.createRestaurant(reqDto, managerId);
    }

}
