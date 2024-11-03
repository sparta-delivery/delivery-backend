package com.sparta.deliverybackend.domain.restaurant.repository;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
