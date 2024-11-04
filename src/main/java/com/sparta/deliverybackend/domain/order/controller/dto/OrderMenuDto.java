package com.sparta.deliverybackend.domain.order.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderMenuDto {
	private Long menuId;
	private Long quantity;
}
