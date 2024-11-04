package com.sparta.deliverybackend.api.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sparta.deliverybackend.common.JwtHelper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidationInterceptor implements HandlerInterceptor {

	private final JwtHelper jwtHelper;

	@Override
	public boolean preHandle(
		HttpServletRequest req,
		HttpServletResponse res,
		Object handler
	) throws RuntimeException {
		try {
			String accessToken = req.getHeader("Authorization");
			jwtHelper.validate(accessToken.substring("Bearer ".length()));
			return true;
		} catch (JwtException e) {
			throw new JwtException(e.getMessage());
		}
	}
}
