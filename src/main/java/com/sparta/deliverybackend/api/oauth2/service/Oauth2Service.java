package com.sparta.deliverybackend.api.oauth2.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliverybackend.api.auth.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.auth.service.AuthService;
import com.sparta.deliverybackend.api.oauth2.config.InMemoryProviderRepository;
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2ProviderProperties;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

	private final MemberRepository memberRepository;
	private final InMemoryProviderRepository inMemoryProviderRepository;
	private final Oauth2ClientService oauthClientService;
	private final AuthService authService;
	
	public LoginResDto loginOrRegister(String providerName, String authorizationCode) throws
		JsonProcessingException {
		Oauth2ProviderType providerType = Oauth2ProviderType.of(providerName);
		Oauth2ProviderProperties provider = inMemoryProviderRepository.findProvider(providerType);
		String oauthAccessToken = oauthClientService.requestAccessToken(authorizationCode, provider);
		OauthMemberProfile memberProfile = oauthClientService.requestMemberProfile(oauthAccessToken, providerName,
			provider);
		if (!memberRepository.existsByOauthId(memberProfile.getOauthId())) {
			String oauthId = memberProfile.getOauthId();
			String email = memberProfile.getEmail();
			String name = memberProfile.getName();
			authService.registerWithOauth(oauthId, email, name);
		}
		return authService.loginWithOauth(memberProfile.getEmail());
	}
}
