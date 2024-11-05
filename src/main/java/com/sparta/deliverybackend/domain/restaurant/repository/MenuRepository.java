package com.sparta.deliverybackend.domain.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;

import java.util.Collection;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantId(Long restaurantId);
}
