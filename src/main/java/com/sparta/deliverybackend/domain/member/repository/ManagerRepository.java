package com.sparta.deliverybackend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.deliverybackend.domain.member.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
	boolean existsByEmail(String email);

	Optional<Manager> findByEmail(String email);
}
