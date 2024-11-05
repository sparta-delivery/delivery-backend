package com.sparta.deliverybackend.api.oauth2.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private final RestTemplate restTemplate;

	@Transactional
	public LoginResDto loginOrRegister(String providerName, String code) throws
		JsonProcessingException {
		Oauth2Provider provider = inMemoryProviderRepository.findProvider(providerName);
		String oauthAccessToken = getToken(code, provider);
		OauthMemberProfile memberProfile = getMemberProfile(oauthAccessToken, providerName, provider);
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

	private String getToken(String code, Oauth2Provider provider) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(provider.getClientId(), provider.getClientSecret());
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", code);
		body.add("client_id", provider.getClientId());
		body.add("client_secret", provider.getClientSecret());
		body.add("grant_type", "authorization_code");
		body.add("redirect_uri", provider.getRedirectUrl());

		RequestEntity<MultiValueMap<String, String>> request = RequestEntity
			.post(provider.getTokenUrl())
			.headers(headers)
			.body(body);

		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
		return jsonNode.get("access_token").asText();
	}

	private OauthMemberProfile getMemberProfile(
		String accessToken,
		String providerName,
		Oauth2Provider provider
	) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> requestHeader = new HttpEntity<>("", headers);
		ResponseEntity<String> response = restTemplate.exchange(
			provider.getUserInfoUrl(),
			HttpMethod.GET,
			requestHeader,
			String.class);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> attributes = mapper.readValue(response.getBody(), Map.class);
		return Oauth2Attributes.extract(providerName, attributes);
	}
}
