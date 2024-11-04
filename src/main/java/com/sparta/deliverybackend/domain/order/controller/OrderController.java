package com.sparta.deliverybackend.domain.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
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

	// 주문 요청
	@PostMapping("/order")
	public ResponseEntity<OrderResponseDto> createOrder(
		VerifiedMember verifiedMember,
		@Valid @RequestBody OrderRequestDto requestDto
	) {

		List<OrderMenuDto> orderMenu = requestDto.getMenus();
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(orderService.createOrder(verifiedMember, orderMenu));
	}

	// 주문 상황 업데이트 (가게 사장님 전용), 이 요청 자체를 수락으로 봄
	// AOP 에 의해 로그 남길 예정 (요청 시각, 가계 id 주문 id)
	@PatchMapping("/order/{orderId}")
	public ResponseEntity<Void> updateOrderStatus(
		@PathVariable Long orderId
	) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 주문 취소 (가게 사장님 전용) , 부가적으로 일정 시간 응답이 없는 경우 진행될 controller
	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<Void> deleteOrder(
		@PathVariable Long orderId
	) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
