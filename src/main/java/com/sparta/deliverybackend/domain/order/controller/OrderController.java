package com.sparta.deliverybackend.domain.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderCancelResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderMenuDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderRequestDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/order")
	public ResponseEntity<OrderResponseDto> createOrder(
		// 주문 요청 (손님 전용)
		VerifiedMember verifiedMember,
		@Valid @RequestBody OrderRequestDto requestDto
	) {

		List<OrderMenuDto> orderMenu = requestDto.getMenus();
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(orderService.createOrder(verifiedMember, orderMenu));
	}

	// 주문 취소는 상태가 WAIT 상태일 때만 취소 가능 (손님, 사장 모두)
	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<OrderCancelResponseDto> deleteOrder(
		VerifiedMember verifiedMember,
		@PathVariable Long orderId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(orderService.cancelOrder(verifiedMember, orderId));
	}

}
