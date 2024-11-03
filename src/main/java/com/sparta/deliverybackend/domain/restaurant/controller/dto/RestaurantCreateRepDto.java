package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RestaurantCreateRepDto {
    private String name;
    private String openTime;
    private String closeTime;
    private int minPrice;
    private Long managerId;

    public RestaurantCreateRepDto(Restaurant restaurant) {
        this.name = restaurant.getName();
        this.openTime = restaurant.getOpenTime();
        this.closeTime = restaurant.getCloseTime();
        this.minPrice = restaurant.getMinPrice();
        this.managerId = restaurant.getManager().getId();
    }
}
