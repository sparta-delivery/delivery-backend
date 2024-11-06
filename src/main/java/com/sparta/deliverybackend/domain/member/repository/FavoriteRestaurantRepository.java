package com.sparta.deliverybackend.domain.member.repository;

import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
    Optional<FavoriteRestaurant> findByRestaurantAndMember(Restaurant restaurant, Member member);

    List<FavoriteRestaurant> findAllByMemberAndDeletedAtIsNull(Member member);
}
