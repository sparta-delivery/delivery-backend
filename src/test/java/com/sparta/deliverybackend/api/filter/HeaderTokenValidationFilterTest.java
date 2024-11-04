package com.sparta.deliverybackend.api.filter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
class HeaderTokenValidationFilterTest {

	private final HeaderTokenValidationFilter headerTokenValidationFilter = spy(new HeaderTokenValidationFilter());

	@Test
	@DisplayName("토큰이 필요한 url로 요청이 들어온 경우, 토큰이 존재하면 성공한다.")
	public void token_validate_success_test() throws ServletException, IOException {
		//given
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/need-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = new MockFilterChain();
		request.addHeader("Authorization", "Bearer TestJwtToken");

		//when
		headerTokenValidationFilter.doFilterInternal(request, response, filterChain);

		//then
		assertThat(headerTokenValidationFilter.isApplicable(request)).isFalse();
		verify(headerTokenValidationFilter, times(1)).doFilterInternal(request, response, filterChain);
	}

	@Test
	@DisplayName("토큰이 필요하지 않은 url로 요청이 들어온 경우, 필터를 통과한다.")
	public void filter_passes_without_token_success_test() throws ServletException, IOException {
		//given
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/auth/need-not-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = new MockFilterChain();

		//when
		headerTokenValidationFilter.doFilterInternal(request, response, filterChain);

		//then
		assertThat(headerTokenValidationFilter.isApplicable(request)).isTrue();
		verify(headerTokenValidationFilter, times(1)).doFilterInternal(request, response, filterChain);
	}

	@Test
	@DisplayName("토큰이 필요한 url로 요청이 들어왔지만 토큰이 없을 경우 예외 발생한다.")
	public void token_validation_exception_test() throws ServletException, IOException {
		//given
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/need-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = new MockFilterChain();

		//when && then
		assertThatThrownBy(() -> headerTokenValidationFilter.doFilterInternal(request, response, filterChain))
			.isInstanceOf(ResponseStatusException.class)
			.hasMessageContaining("토큰이 없습니다.");
	}
}