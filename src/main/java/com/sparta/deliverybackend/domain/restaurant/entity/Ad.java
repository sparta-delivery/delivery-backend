package com.sparta.deliverybackend.domain.restaurant.entity;

import java.time.LocalDateTime;

import com.sparta.deliverybackend.domain.BaseTimeStampEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "advertiesment")
public class Ad extends BaseTimeStampEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	// 광고 생성 시, 광고 비활성 상태
	@Column(nullable = false)
	private boolean isActive = false;

	// 광고 중지
	@Column
	private LocalDateTime deletedAt;

	public void activeAds(){
		this.isActive = true;
		this.deletedAt = null;
	}

	public void inActiveAds(){
		this.isActive = false;
		this.deletedAt = LocalDateTime.now();
	}
}
