package com.sparta.deliverybackend.domain.member.controller.dto;

import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRestaurantAddRespDto {
    private Long id;
    private Long restaurantId;

    public FavoriteRestaurantAddRespDto(FavoriteRestaurant favoriteRestaurant){
        this.id = favoriteRestaurant.getId();
        this.restaurantId = favoriteRestaurant.getRestaurant().getId();
    }
}
