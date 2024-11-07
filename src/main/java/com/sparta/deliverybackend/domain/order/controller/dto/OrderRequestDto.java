package com.sparta.deliverybackend.domain.order.controller.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderRequestDto {
	// 메뉴
	@NotNull(message = "빈 카트로 주문할 수 없습니다.")
	private List<OrderMenuDto> menus;

}
