package com.sparta.deliverybackend.domain.restaurant.repository;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    long countByManagerId(Long managerId);
    Page<Restaurant> findAllByDeletedAtIsNull(Pageable pageable);
    long countByManagerIdAndDeletedAtIsNull(Long managerId);
}
