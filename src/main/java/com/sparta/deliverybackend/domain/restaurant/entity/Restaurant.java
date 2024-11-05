package com.sparta.deliverybackend.domain.restaurant.entity;

import java.time.LocalDateTime;

import com.sparta.deliverybackend.domain.BaseTimeStampEntity;
import com.sparta.deliverybackend.domain.member.entity.Manager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Entity
@Table(name = "restaurant")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Restaurant extends BaseTimeStampEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String openTime;

	@Column
	private String closeTime;

	@Column
	private Integer minPrice;

	@Column
	private LocalDateTime deletedAt;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Manager manager;

	public Restaurant(String name, String openTime, String closeTime, int minPrice, Manager managerId) {
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minPrice = minPrice;
		this.manager = managerId;
	}


	public void modify(String name, String openTime, String closeTime, int minPrice, Manager managerId){
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minPrice = minPrice;
		this.manager = managerId;
	}

	public void delete(){
		this.deletedAt = LocalDateTime.now();
	}

	public Restaurant(Long restaurantId) {
		this.id = restaurantId;
	}
}
