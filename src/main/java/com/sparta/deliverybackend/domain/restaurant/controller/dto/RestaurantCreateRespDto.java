package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantCreateRespDto {
    private Long id;
    private String name;
    private String openTime;
    private String closeTime;
    private int minPrice;
    private Long managerId;

    public RestaurantCreateRespDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.openTime = restaurant.getOpenTime();
        this.closeTime = restaurant.getCloseTime();
        this.minPrice = restaurant.getMinPrice();
        this.managerId = restaurant.getManager().getId();
    }
}
