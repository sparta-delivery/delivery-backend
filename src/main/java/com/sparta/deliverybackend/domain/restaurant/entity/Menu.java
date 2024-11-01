package com.sparta.deliverybackend.domain.restaurant.entity;

import com.sparta.deliverybackend.domain.BaseTimeStampEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseTimeStampEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private Integer price;

	@Column
	private String description;

	@Column
	@Enumerated(value = EnumType.STRING)
	private CuisineType cuisineType;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
}
