package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RestaurantCreateReqDto {

    private String name;
    private String openTime;
    private String closeTime;
    private int minPrice;
    private Long managerId;

    public RestaurantCreateReqDto(String name, String openTime, String closeTime, int minPrice, Long managerId){
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minPrice = minPrice;
        this.managerId = managerId;
    }
}
