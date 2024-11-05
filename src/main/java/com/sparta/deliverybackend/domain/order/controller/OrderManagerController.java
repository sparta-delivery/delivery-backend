package com.sparta.deliverybackend.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderCancelResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderUpdateResponseDto;
import com.sparta.deliverybackend.domain.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class OrderManagerController {

	private final OrderService orderService;

	// 주문 상황 업데이트 (가게 사장님 전용), 이 요청 자체를 수락으로 봄
	// AOP 에 의해 로그 남길 예정 (요청 시각, 가계 id 주문 id)
	// 주문 완료 시 orders_menu 테이블에서 삭제 예정
	@PatchMapping("/order/{orderId}")
	public ResponseEntity<OrderUpdateResponseDto> updateOrderStatus(
		VerifiedMember verifiedMember,
		@PathVariable Long orderId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(orderService.updateOrderStatus(verifiedMember, orderId));
	}

	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<OrderCancelResponseDto> deleteOrder(
		VerifiedMember verifiedMember,
		@PathVariable Long orderId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(orderService.cancelOrder(verifiedMember, orderId));
	}
}
