package com.sparta.deliverybackend.api.interceptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.sparta.deliverybackend.common.JwtHelper;

@ExtendWith(MockitoExtension.class)
class JwtValidationInterceptorTest {

	@Mock
	private JwtHelper jwtHelper;

	@InjectMocks
	private JwtValidationInterceptor interceptor;

	@Test
	@DisplayName("인터셉터가 호출 됐지만 토큰이 없는 경우 예외가 발생한다.")
	public void interceptor_no_accessToken_exception_test() {
		//given
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		//when && then
		assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("잘못된 토큰이 들어온 경우 예외가 발생한다.")
	public void interceptor_invalid_accessToken_exception_test() {
		//given
		String invalidToken = "Bearer invalid";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.addHeader("Authorization", invalidToken);

		doThrow(new RuntimeException()).when(jwtHelper).validate(any());

		//when && then
		assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("인터셉터 정상 통과 테스트.")
	public void interceptor_success_test() {
		//given
		String validToken = "Bearer valid";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.addHeader("Authorization", validToken);

		doNothing().when(jwtHelper).validate(any());

		//when
		boolean result = interceptor.preHandle(request, response, null);

		//then
		assertThat(result).isTrue();
	}
}