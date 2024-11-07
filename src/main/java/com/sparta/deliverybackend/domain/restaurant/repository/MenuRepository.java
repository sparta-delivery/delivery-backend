package com.sparta.deliverybackend.domain.restaurant.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {
	List<Menu> findByRestaurantId(Long restaurantId);

	Page<Menu> findAll(Specification<Menu> spec, Pageable pageable);
}
