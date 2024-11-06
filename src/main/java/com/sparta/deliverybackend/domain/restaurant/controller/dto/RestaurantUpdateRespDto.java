package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class RestaurantUpdateRespDto {
    private Long id;
    private LocalDateTime updatedAt;

    public RestaurantUpdateRespDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.updatedAt = restaurant.getUpdatedAt();
    }

}
