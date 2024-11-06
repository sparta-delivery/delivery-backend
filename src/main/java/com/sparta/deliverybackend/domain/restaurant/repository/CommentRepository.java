package com.sparta.deliverybackend.domain.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.deliverybackend.domain.restaurant.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
