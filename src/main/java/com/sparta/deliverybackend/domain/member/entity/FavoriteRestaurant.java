package com.sparta.deliverybackend.domain.member.entity;

import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

	@Column
	@Setter
	private LocalDateTime deletedAt;

	public FavoriteRestaurant(Restaurant restaurantId, Member memberId) {
		this.restaurant = restaurantId;
		this.member = memberId;
	}

	public void delete(){
		this.deletedAt = LocalDateTime.now();
	}
}
