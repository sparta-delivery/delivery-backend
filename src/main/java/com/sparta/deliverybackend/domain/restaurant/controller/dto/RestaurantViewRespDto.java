package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RestaurantViewRespDto {
    private Long id;
    private String name;
    private String openTime;
    private String closeTime;
    private int minPrice;
    private Long managerId;
    private List<MenuRespDto> menus;

    public RestaurantViewRespDto(Restaurant restaurant, List<MenuRespDto> menus) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.openTime = restaurant.getOpenTime();
        this.closeTime = restaurant.getCloseTime();
        this.minPrice = restaurant.getMinPrice();
        this.managerId = restaurant.getManager().getId();
        this.menus = menus;
    }
}
