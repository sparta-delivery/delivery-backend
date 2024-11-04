package com.sparta.deliverybackend.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.deliverybackend.domain.order.entity.OrderMenu;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
}
