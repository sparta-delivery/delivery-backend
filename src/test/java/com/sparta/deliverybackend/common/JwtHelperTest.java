package com.sparta.deliverybackend.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.sparta.deliverybackend.domain.member.entity.Member;

import io.jsonwebtoken.JwtException;

class JwtHelperTest {
	private final JwtHelper jwtHelper = new JwtHelper();
	private final String testTokenSecretKey = "kimwanyoungjwtaccesstokensecretkeytest";
	private final Long testTokenExpiresIn = 1800000L;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(jwtHelper, "tokenSecretKey", testTokenSecretKey);
		ReflectionTestUtils.setField(jwtHelper, "tokenExpiresIn", testTokenExpiresIn);
		jwtHelper.init();
	}

	@Test
	@DisplayName("토큰 생성 정상 테스트")
	public void generate_token_success_test() {
		//given
		Member member = Member.builder()
			.id(1L)
			.build();

		//when
		String accessToken = jwtHelper.generateAccessToken(member);
		Long id = jwtHelper.extractMemberId(accessToken);

		//then
		assertThat(id).isEqualTo(member.getId());
	}

	@Test
	@DisplayName("잘못된 토큰이 들어오면 예외가 발생한다.")
	public void validate_token_success_test() {
		//given
		String invalidToken = "invalid token test";

		//when && then
		assertThatThrownBy(() -> jwtHelper.validate(invalidToken))
			.isInstanceOf(JwtException.class);
	}
}