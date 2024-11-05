package com.sparta.deliverybackend.api.oauth2.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliverybackend.api.auth.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.oauth2.config.InMemoryProviderRepository;
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;
import com.sparta.deliverybackend.common.JwtHelper;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.global.security.PasswordEncoder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtHelper jwtHelper;
	private final InMemoryProviderRepository inMemoryProviderRepository;
	private final OauthClientService oauthClientService;

	@Transactional
	public LoginResDto loginOrRegister(String providerName, String authorizationCode) throws
		JsonProcessingException {
		Oauth2Provider provider = inMemoryProviderRepository.findProvider(providerName);
		String oauthAccessToken = oauthClientService.requestAccessToken(authorizationCode, provider);
		OauthMemberProfile memberProfile = oauthClientService.requestMemberProfile(oauthAccessToken, providerName,
			provider);
		if (!memberRepository.existsByOauthId(memberProfile.getOauthId())) {
			register(memberProfile);
		}
		return login(memberProfile);
	}

	private void register(OauthMemberProfile memberProfile) {
		memberRepository.findByEmail(memberProfile.getEmail())
			.ifPresentOrElse(
				savedMember -> savedMember.updateOauthId(memberProfile.getOauthId()),
				() -> {
					String password = passwordEncoder.encode(UUID.randomUUID().toString());
					Member oauthMember = new Member(memberProfile, password);
					memberRepository.save(oauthMember);
				});
	}

	private LoginResDto login(OauthMemberProfile memberProfile) {
		Member member = memberRepository.findByEmail(memberProfile.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("가입된 유저를 찾을 수 없습니다."));
		String accessToken = jwtHelper.generateAccessToken(member);
		return new LoginResDto(accessToken);
	}
}
