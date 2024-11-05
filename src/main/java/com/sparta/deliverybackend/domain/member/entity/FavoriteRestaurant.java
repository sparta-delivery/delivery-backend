package com.sparta.deliverybackend.domain.member.entity;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "favorite_restaurant")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRestaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public FavoriteRestaurant(Restaurant restaurantId, Member memberId) {
		this.restaurant = restaurantId;
		this.member = memberId;

	}
}
