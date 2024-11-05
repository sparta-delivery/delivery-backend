package com.sparta.deliverybackend.domain.order.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderUpdateResponseDto;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "주문 상태 관련 로그 ")
@Aspect
@Component
public class OrderLoggingAspect {

	@AfterReturning(value = "execution(* com.sparta.deliverybackend.domain.order.service.OrderService.createOrder(..))", returning = "result")
	public void logAfterCreateOrder(JoinPoint joinPoint, Object result) {
		if (result instanceof OrderResponseDto orderResponse) {
			String restaurantName = orderResponse.getRestaurantName();
			OrderStatus orderStatus = orderResponse.getOrderStatus();

			log.info("주문 생성 - 요청 시각: {}, 가게 이름: {}, 주문 상태: {}", LocalDateTime.now(), restaurantName, orderStatus);
		}
	}

	@AfterReturning(value = "execution(* com.sparta.deliverybackend.domain.order.service.OrderService.updateOrderStatus(..))", returning = "result")
	public void logAfterUpdateOrderStatus(JoinPoint joinPoint, Object result) {
		if (result instanceof OrderUpdateResponseDto orderUpdateResponse) {
			Long orderId = orderUpdateResponse.getOrderId();
			OrderStatus orderStatus = orderUpdateResponse.getStatus();

			log.info("주문 상태 변경 - 요청 시각: {}, 주문 ID: {}, 주문 상태: {}", LocalDateTime.now(), orderId, orderStatus);
		}
	}
}
