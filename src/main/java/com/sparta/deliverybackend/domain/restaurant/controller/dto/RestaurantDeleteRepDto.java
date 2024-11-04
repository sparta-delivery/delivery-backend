package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class RestaurantDeleteRepDto {
    private Long id;
    private LocalDateTime deletedAt;


}
