package com.sparta.deliverybackend.domain.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
