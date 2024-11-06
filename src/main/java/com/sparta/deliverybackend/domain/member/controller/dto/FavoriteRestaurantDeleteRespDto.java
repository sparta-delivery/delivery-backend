package com.sparta.deliverybackend.domain.member.controller.dto;

import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class FavoriteRestaurantDeleteRespDto {
    private Long restaurantId;
    private LocalDateTime deletedAt;

    public FavoriteRestaurantDeleteRespDto(FavoriteRestaurant favoriteRestaurant){
        this.restaurantId = favoriteRestaurant.getId();
        this.deletedAt = favoriteRestaurant.getDeletedAt();
    }
}
