package com.sparta.deliverybackend.domain.restaurant.entity;

public enum CuisineType {
	KOREAN("KOREAN"),
	WESTERN("WESTERN"),
	CHINESE("CHINESE"),
	JAPANESE("JAPANESE");

	private final String displayName;

	CuisineType(String displayName) {
		this.displayName = displayName;
	}
}
