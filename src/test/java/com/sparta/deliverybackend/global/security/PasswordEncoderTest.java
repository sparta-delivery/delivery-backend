package com.sparta.deliverybackend.global.security;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordEncoderTest {

	private final PasswordEncoder passwordEncoder = new PasswordEncoder();

	@Test
	@DisplayName("비밀번호 정상 encode 테스트")
	public void encode_success_test() {
		//given
		String rawPassword = "raw password";

		//when
		String encodedPassword = passwordEncoder.encode(rawPassword);
		String sameEncodedPassword = passwordEncoder.encode(rawPassword);

		//then
		assertThat(encodedPassword).isNotEqualTo(sameEncodedPassword);
	}

	@Test
	@DisplayName("비밀번호 비교 정상 테스트")
	public void matched_success_test() {
		//given
		String rawPassword = "raw password";

		//when
		String encodedPassword = passwordEncoder.encode(rawPassword);
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

		//then
		assertThat(matches).isTrue();
	}
}