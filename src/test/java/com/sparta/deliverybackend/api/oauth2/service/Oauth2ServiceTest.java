package com.sparta.deliverybackend.api.oauth2.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliverybackend.api.auth.service.AuthService;
import com.sparta.deliverybackend.api.oauth2.config.InMemoryProviderRepository;
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2ProviderProperties;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class Oauth2ServiceTest {

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private InMemoryProviderRepository inMemoryProviderRepository;
	@Mock
	private Oauth2ClientService oauthClientService;
	@Mock
	private AuthService authService;
	@Mock
	Oauth2ProviderProperties providerProperties;
	@InjectMocks
	private Oauth2Service oauth2Service;

	@Test
	@DisplayName("정상적인 소셜 로그인 회원가입 테스트")
	public void oauth_register_success_test() throws JsonProcessingException {
		// given
		String providerName = "google";
		String authorizationCode = "authCode";
		String oauthAccessToken = "accessToken";
		String oauthId = "12345";
		String email = "user@example.com";
		String name = "Test User";

		Oauth2ProviderType providerType = Oauth2ProviderType.of(providerName);
		Oauth2ProviderProperties provider = Oauth2ProviderProperties.builder().build();
		OauthMemberProfile memberProfile = OauthMemberProfile.builder()
			.oauthId(oauthId)
			.name(name)
			.email(email)
			.build();

		when(inMemoryProviderRepository.findProvider(providerType))
			.thenReturn(provider);
		when(oauthClientService.requestAccessToken(authorizationCode, provider))
			.thenReturn(oauthAccessToken);
		when(oauthClientService.requestMemberProfile(oauthAccessToken, providerName, provider))
			.thenReturn(memberProfile);
		when(memberRepository.existsByOauthId(memberProfile.getOauthId()))
			.thenReturn(false);

		// when
		oauth2Service.loginOrRegister(providerName, authorizationCode);

		// then
		verify(authService, times(1)).registerWithOauth(oauthId, email, name);
		verify(authService, times(1)).loginWithOauth(email);
	}

	@Test
	@DisplayName("일반 유저로 이미 가입했다면, login만 호출된다.")
	public void oauth_login_success_test() throws JsonProcessingException {
		// given
		String providerName = "google";
		String authorizationCode = "authCode";
		String oauthAccessToken = "accessToken";
		String oauthId = "12345";
		String email = "user@example.com";
		String name = "Test User";

		Oauth2ProviderType providerType = Oauth2ProviderType.of(providerName);
		Oauth2ProviderProperties provider = Oauth2ProviderProperties.builder().build();
		OauthMemberProfile memberProfile = OauthMemberProfile.builder()
			.oauthId(oauthId)
			.name(name)
			.email(email)
			.build();

		when(inMemoryProviderRepository.findProvider(providerType))
			.thenReturn(provider);
		when(oauthClientService.requestAccessToken(authorizationCode, provider))
			.thenReturn(oauthAccessToken);
		when(oauthClientService.requestMemberProfile(oauthAccessToken, providerName, provider))
			.thenReturn(memberProfile);
		when(memberRepository.existsByOauthId(memberProfile.getOauthId()))
			.thenReturn(true);

		// when
		oauth2Service.loginOrRegister(providerName, authorizationCode);

		// then
		verify(authService, times(0)).registerWithOauth(oauthId, email, name);
		verify(authService, times(1)).loginWithOauth(email);
	}
}