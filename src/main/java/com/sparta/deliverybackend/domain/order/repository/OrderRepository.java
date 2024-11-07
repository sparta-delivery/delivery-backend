package com.sparta.deliverybackend.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.deliverybackend.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
