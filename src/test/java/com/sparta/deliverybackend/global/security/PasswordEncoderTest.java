package com.sparta.deliverybackend.global.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("비밀번호 정상 encode 테스트")
	public void encode_success_test() {
		//given
		String rawPassword = "raw password";

		when(passwordEncoder.encode(any(String.class)))
			.thenReturn("encoded password");

		//when
		String encodedPassword = passwordEncoder.encode(rawPassword);

		//then
		assertThat(encodedPassword).isEqualTo("encoded password");
	}

	@Test
	@DisplayName("비밀번호 비교 정상 테스트")
	public void matched_success_test() {
		//given
		String rawPassword = "raw password";

		when(passwordEncoder.encode(any(String.class)))
			.thenReturn("encoded password");
		when(passwordEncoder.matches(eq("raw password"), eq("encoded password")))
			.thenReturn(true);

		//when
		String encodedPassword = passwordEncoder.encode(rawPassword);
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

		//then
		assertThat(matches).isTrue();
	}
}