package com.sparta.deliverybackend.domain.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.deliverybackend.domain.restaurant.entity.Ad;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
}
