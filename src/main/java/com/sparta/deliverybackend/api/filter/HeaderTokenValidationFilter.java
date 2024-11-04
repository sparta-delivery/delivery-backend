package com.sparta.deliverybackend.api.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@Order(0)
@RequiredArgsConstructor
public class HeaderTokenValidationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws
		IOException, ServletException {
		if (this.isApplicable(req)) {
			chain.doFilter(req, res);
			return;
		}
		String accessToken = Optional.ofNullable(req.getHeader("Authorization"))
			.map(header -> header.substring("Bearer ".length()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."));
		chain.doFilter(req, res);
	}

	public boolean isApplicable(HttpServletRequest req) {
		return req.getRequestURI().startsWith("/api/auth") ||
			req.getRequestURI().startsWith("/api/oauth2") ||
			req.getRequestURI().startsWith("/error") ||
			req.getRequestURI().startsWith("/favicon");
	}
}
