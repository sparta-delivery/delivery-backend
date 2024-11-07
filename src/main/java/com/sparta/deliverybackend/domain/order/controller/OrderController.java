package com.sparta.deliverybackend.domain.order.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderCancelResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderMenuDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderRequestDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.interceptor.ReadableRequestWrapper;
import com.sparta.deliverybackend.domain.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

	private final OrderService orderService;
	private final ObjectMapper objectMapper;

	@PostMapping("/order")
	public ResponseEntity<OrderResponseDto> createOrder(
		// 주문 요청 (손님 전용)
		VerifiedMember verifiedMember,
		HttpServletRequest request

	) throws IOException {
		ReadableRequestWrapper wrappedRequest = (ReadableRequestWrapper)request.getAttribute("wrappedRequest");
		OrderRequestDto orderRequest = objectMapper.readValue(wrappedRequest.getReader(), OrderRequestDto.class);
		List<OrderMenuDto> orderMenu = orderRequest.getMenus();
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
