package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OptionReqDto {

	@NotNull(message = "옵션명을 입력해 주세요.")
	private String name;

	@NotNull(message = "추가 가격을 입력해 주세요.")
	private Integer additionalPrice;
}
