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

import com.sparta.deliverybackend.api.auth.controller.dto.LoginReqDto;
import com.sparta.deliverybackend.api.auth.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.auth.controller.dto.RegisterReqDto;
import com.sparta.deliverybackend.api.auth.service.AuthService;
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

	@Test
	@DisplayName("Oauth를 통한 login 시도 시 유저가 없으면 예외가 발생한다.")
	public void oauth_login_exception_test() {
		//given
		String email = "dost@not.exists";
		when(memberRepository.findByEmail(email))
			.thenReturn(Optional.empty());

		//when && then
		assertThatThrownBy(() -> authService.loginWithOauth(email))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가입된 유저를 찾을 수 없습니다");
	}

	@Test
	@DisplayName("Oauth를 통한 register 시도 시 이미 유저가 존재하면 oauthId가 업데이트 된다.")
	public void oauth_register_update_oauthId_test() {
		//given
		String oauthId = "12345";
		String name = "kimwanyoung";
		String email = "user@email.com";
		Member member = Member.builder()
			.oauthId(null)
			.nickname(name)
			.email(email)
			.build();

		when(memberRepository.findByEmail(email))
			.thenReturn(Optional.of(member));

		//when
		authService.registerWithOauth(oauthId, email, name);

		//then
		assertThat(member.getOauthId()).isEqualTo(oauthId);
	}

	@Test
	@DisplayName("Oauth로 최초 회원가입 시도 하면 새로운 멤버가 저장된다.")
	public void oauth_new_member_register_success_test() {
		//given
		String oauthId = "12345";
		String name = "kimwanyoung";
		String email = "user@email.com";

		when(memberRepository.findByEmail(email))
			.thenReturn(Optional.empty());

		//when
		authService.registerWithOauth(oauthId, email, name);

		//then
		verify(memberRepository, times(1)).save(any(Member.class));
	}

	@Test
	@DisplayName("Oauth로 정상적인 login호출 시 LoginResDto가 반환된다.")
	public void oauth_login_success_test() {
		//given
		String oauthId = "12345";
		String name = "kimwanyoung";
		String email = "user@email.com";
		Member member = Member.builder()
			.oauthId(oauthId)
			.nickname(name)
			.email(email)
			.build();

		when(memberRepository.findByEmail(email))
			.thenReturn(Optional.of(member));
		when(jwtHelper.generateAccessToken(member))
			.thenReturn("validAccessToken");

		//when
		LoginResDto result = authService.loginWithOauth(email);

		//then
		assertThat(result.accessToken()).isEqualTo("validAccessToken");
		verify(memberRepository, times(1)).findByEmail(eq(email));
	}
}