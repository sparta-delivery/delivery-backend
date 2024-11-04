package com.sparta.deliverybackend.domain.restaurant.entity;

import com.sparta.deliverybackend.domain.BaseTimeStampEntity;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuCreateRequestDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuResponseDto;

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
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu")
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

	@Setter
	@Column(name = "is_deleted")
	private boolean deleted = false;

	public static Menu from(MenuCreateRequestDto menuCreateRequestDto) {
		Menu menu = new Menu();
		menu.initData(menuCreateRequestDto);
		return menu;
	}

	private void initData(MenuCreateRequestDto menuCreateRequestDto) {
		this.name = menuCreateRequestDto.getName();

		try{
			this.cuisineType = CuisineType.valueOf(menuCreateRequestDto.getCuisineType());
		} catch(IllegalArgumentException e){
			throw new IllegalArgumentException("음식 타입이 설정되지 않았습니다.: " + menuCreateRequestDto.getCuisineType());
		}

		this.price = menuCreateRequestDto.getPrice();
		this.description = menuCreateRequestDto.getDescription();
		this.restaurant = new Restaurant(menuCreateRequestDto.getRestaurantId());
	}

	public MenuResponseDto to() {
		return MenuResponseDto.builder()
			.id(this.id)
			.name(this.name)
			.price(this.price)
			.description(this.description)
			.cuisineType(this.cuisineType)
			.restaurantId(this.restaurant.getId())
			.createdAt(this.getCreatedAt())
			.updatedAt(this.getUpdatedAt())
			.build();
	}
}
