package com.sparta.deliverybackend.domain.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sparta.deliverybackend.domain.order.interceptor.OrderStatusValidationInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OrderWebConfig implements WebMvcConfigurer {

	private final OrderStatusValidationInterceptor orderStatusValidationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(orderStatusValidationInterceptor)
			.addPathPatterns("/api/order/**");

	}
}
