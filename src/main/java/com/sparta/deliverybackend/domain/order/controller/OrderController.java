package com.sparta.deliverybackend.domain.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderMenuDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderRequestDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

	private final OrderService orderService;

	// 주문 요청
	@PostMapping("/order")
	public ResponseEntity<OrderResponseDto> createOrder(
		@RequestBody OrderRequestDto requestDto,
		Member member) {

		List<OrderMenuDto> orderMenu = requestDto.getMenus();
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(orderService.createOrder(member, orderMenu));
	}

	// 주문 상황 업데이트 (가게 사장님 전용)

}
