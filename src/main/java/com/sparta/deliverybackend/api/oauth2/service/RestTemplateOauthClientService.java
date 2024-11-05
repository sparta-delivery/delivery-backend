package com.sparta.deliverybackend.api.oauth2.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

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
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestTemplateOauthClientService implements OauthClientService {
	
	private final RestTemplate restTemplate;

	@Override
	public String requestAccessToken(String code, Oauth2Provider provider) throws JsonProcessingException {
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

	@Override
	public OauthMemberProfile requestMemberProfile(String accessToken, String providerName, Oauth2Provider provider
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
