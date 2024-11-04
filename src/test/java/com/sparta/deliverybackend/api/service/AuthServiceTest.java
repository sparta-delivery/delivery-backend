package com.sparta.deliverybackend.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.deliverybackend.api.controller.dto.LoginReqDto;
import com.sparta.deliverybackend.api.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.controller.dto.RegisterReqDto;
import com.sparta.deliverybackend.common.JwtHelper;
import com.sparta.deliverybackend.domain.member.entity.JoinPath;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.global.security.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private JwtHelper jwtHelper;
	@Mock
	private PasswordEncoder passwordEncoder;
	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("회원가입 - 이미 존재하는 이메일로 회원가입을 진행하면 예외가 발생한다.")
	public void register_email_already_exist_exception_test() {
		//given
		RegisterReqDto req = new RegisterReqDto("tester", "email@email.com", JoinPath.BASIC, "password");
		when(memberRepository.existsByEmail(any())).thenReturn(true);

		//when && then
		assertThatThrownBy(() -> authService.register(req))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 존재하는 이메일 입니다.");
	}

	@Test
	@DisplayName("회원가입 - 정상적인 회원가입 요청이 들어오면 memberRepository.save()가 한 번 호출된다.")
	public void valid_register_request_test() {
		//given
		RegisterReqDto req = new RegisterReqDto("tester", "email@email.com", JoinPath.BASIC, "password");
		when(memberRepository.existsByEmail(any())).thenReturn(false);

		//when
		authService.register(req);

		//then
		verify(memberRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("로그인 - 존재하지 않는 유저가 로그인을 시도하면 예외가 발생한다.")
	public void login_member_is_not_exist_exception_test() {
		//given
		LoginReqDto req = new LoginReqDto("email@email.con", "password");
		when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

		//when && then
		assertThatThrownBy(() -> authService.login(req))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("존재하지 않는 유저입니다.");
	}

	@Test
	@DisplayName("로그인 - 비밀번호가 일치하지 않으면 예외가 발생한다.")
	public void password_not_matches_exception_test() {
		//given
		LoginReqDto req = new LoginReqDto("email@email.com", "password");
		Member savedMember = Member.builder()
			.email("email@email.com")
			.password("encodedpassword")
			.build();
		when(memberRepository.findByEmail(req.email())).thenReturn(Optional.of(savedMember));
		when(passwordEncoder.matches("password", "encodedpassword")).thenReturn(false);

		//when && then
		assertThatThrownBy(() -> authService.login(req))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비밀번호가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("로그인 - 정상적으로 요청 되면 accessToken이 발급된다.")
	public void valid_login_request_test() {
		//given
		LoginReqDto req = new LoginReqDto("email@email.com", "password");
		Member savedMember = Member.builder()
			.email("email@email.com")
			.password("encodedpassword")
			.build();
		when(memberRepository.findByEmail(req.email())).thenReturn(Optional.of(savedMember));
		when(passwordEncoder.matches("password", "encodedpassword")).thenReturn(true);
		when(jwtHelper.generateAccessToken(any())).thenReturn("accessToken");

		//when
		LoginResDto response = authService.login(req);

		//then
		assertThat(response.accessToken()).isEqualTo("accessToken");
		verify(jwtHelper, times(1)).generateAccessToken(any());
		verify(passwordEncoder, times(1)).matches(any(), any());
		verify(memberRepository, times(1)).findByEmail(any());
	}
}