package com.sparta.deliverybackend.common;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.sparta.deliverybackend.domain.member.entity.Member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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

		//then
		String subject = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(testTokenSecretKey.getBytes(StandardCharsets.UTF_8)))
			.build()
			.parseClaimsJws(accessToken)
			.getBody()
			.getSubject();

		assertThat(Long.valueOf(subject)).isEqualTo(member.getId());
	}
}