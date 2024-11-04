package com.sparta.deliverybackend.api.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sparta.deliverybackend.api.controller.resolver.VerifiedMemberArgumentResolver;
import com.sparta.deliverybackend.api.interceptor.JwtValidationInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final JwtValidationInterceptor jwtValidationInterceptor;
	private final VerifiedMemberArgumentResolver verifiedMemberArgumentResolver;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtValidationInterceptor)
			.excludePathPatterns("/api/auth/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(verifiedMemberArgumentResolver);
	}
}
