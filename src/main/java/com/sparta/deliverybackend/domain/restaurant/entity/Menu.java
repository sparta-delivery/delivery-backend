package com.sparta.deliverybackend.domain.restaurant.entity;

import java.util.List;

import javax.swing.text.html.Option;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliverybackend.domain.BaseTimeStampEntity;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.OptionReqDto;

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

	// JSON 형식으로 저장되는 옵션 컬럼
	@Column(columnDefinition = "TEXT")
	private String options;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// 옵션을 JSON 문자열로 직렬화하여 저장
	public void setOptions(List<Option> optionList) throws JsonProcessingException {
		this.options = objectMapper.writeValueAsString(optionList);
	}

	// JSON 문자열을 옵션 객체 리스트로 역직렬화하여 반환
	public List<OptionReqDto> getOptions() throws JsonProcessingException {
		return objectMapper.readValue(this.options, new TypeReference<List<OptionReqDto>>() {

		});
	}

	public static Menu from(MenuCreateReqDto menuCreateReqDto, Restaurant restaurant, Manager manager) {
		return Menu.builder()
			.name(menuCreateReqDto.getName())
			.price(menuCreateReqDto.getPrice())
			.description(menuCreateReqDto.getDescription())
			.cuisineType(CuisineType.valueOf(menuCreateReqDto.getCuisineType()))
			.restaurant(restaurant)
			.build();
	}

	public void isUpdated(String name, Integer price, String description, CuisineType cuisineType) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.cuisineType = cuisineType;
	}

	public void isDeleted(){
		this.deleted = true;
	}

	public void updateOptions(List<OptionReqDto> newOptions){
		try{
			this.options = objectMapper.writeValueAsString(newOptions);
		} catch (JsonProcessingException e){
			throw new RuntimeException("옵션 업데이트 중 오류가 발생했습니다.", e);
		}
	}

	public MenuRespDto to() {
		return MenuRespDto.builder()
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
