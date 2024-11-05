package com.sparta.deliverybackend.domain.member.repository;

import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
    FavoriteRestaurant findByRestaurantId(Long restaurantId);

}
