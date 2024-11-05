package com.sparta.deliverybackend.domain.member.repository;

import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
}
