package com.sparta.deliverybackend.domain.order.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.sparta.deliverybackend.domain.order.controller.dto.OrderCancelResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderUpdateResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "주문 상태 관련 로그 ")
@Aspect
@Component
public class OrderLoggingAspect {

	@AfterReturning(value = "execution(* com.sparta.deliverybackend.domain.order.service.OrderService.*Order(..))", returning = "result")
	public void logAfterOrderMethods(JoinPoint joinPoint, Object result) {
		LocalDateTime requestTime = LocalDateTime.now();

		if (result instanceof OrderResponseDto orderResponse) {
			log.info("주문 생성 - 요청 시각: {}, 가게 이름: {}, 주문 상태: {}",
				requestTime, orderResponse.getRestaurantName(), orderResponse.getOrderStatus());
		}
		if (result instanceof OrderUpdateResponseDto orderUpdateResponse) {
			log.info("주문 상태 변경 - 요청 시각: {}, 주문 ID: {}, 주문 상태: {}",
				requestTime, orderUpdateResponse.getOrderId(), orderUpdateResponse.getStatus());
		}
		if (result instanceof OrderCancelResponseDto orderCancelResponse) {
			log.info("주문 취소 - 요청 시각: {}, 주문 ID: {}, 주문 상태: {}",
				requestTime, orderCancelResponse.getOrderId(), orderCancelResponse.getStatus());
		}
	}
}
