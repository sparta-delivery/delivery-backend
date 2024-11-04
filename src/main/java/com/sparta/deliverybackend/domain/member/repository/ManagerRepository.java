package com.sparta.deliverybackend.domain.member.repository;

import com.sparta.deliverybackend.domain.member.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
