package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class RestaurantDeleteRepDto {
    private Long id;
    private LocalDateTime deletedAt;

    public RestaurantDeleteRepDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.deletedAt = restaurant.getDeletedAt();
    }
}
