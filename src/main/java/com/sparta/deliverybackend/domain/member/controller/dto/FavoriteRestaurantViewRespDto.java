package com.sparta.deliverybackend.domain.member.controller.dto;

import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRestaurantViewRespDto {
    private Long id;
    private Long restaurantId;
    private Long memberId;

    public FavoriteRestaurantViewRespDto(FavoriteRestaurant favoriteRestaurant){
        this.id = favoriteRestaurant.getId();
        this.restaurantId = favoriteRestaurant.getRestaurant().getId();
        this.memberId = favoriteRestaurant.getMember().getId();
    }
}
