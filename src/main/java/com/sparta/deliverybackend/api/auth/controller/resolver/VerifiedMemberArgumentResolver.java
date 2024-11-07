package com.sparta.deliverybackend.api.auth.controller.resolver;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.common.JwtHelper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VerifiedMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtHelper jwtHelper;

	@Override
	public VerifiedMember resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		String accessToken = Objects.requireNonNull(webRequest.getHeader("Authorization"))
			.substring("Bearer ".length());
		Long memberId = jwtHelper.extractMemberId(accessToken);
		return new VerifiedMember(memberId);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(VerifiedMember.class);
	}
}
