package com.sparta.deliverybackend.api.oauth2.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliverybackend.api.auth.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;
import com.sparta.deliverybackend.api.oauth2.repository.InMemoryProviderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

	private final InMemoryProviderRepository inMemoryProviderRepository;
	private final RestTemplate restTemplate;

	public LoginResDto login(String providerName, String code) throws JsonProcessingException {
		Oauth2Provider provider = inMemoryProviderRepository.findProvider(providerName);
		// TODO: access token 가져오기
		String accessToken = getToken(code, provider);
		return new LoginResDto(accessToken);
	}

	private String getToken(String code, Oauth2Provider provider) throws JsonProcessingException {
		URI uri = UriComponentsBuilder
			.fromUriString(provider.getTokenUrl())
			.encode()
			.build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(provider.getClientId(), provider.getClientSecret());
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", code);
		body.add("grant_type", "authorization_code");
		body.add("redirect_uri", provider.getRedirectUrl());

		RequestEntity<MultiValueMap<String, String>> request = RequestEntity
			.post(uri)
			.headers(headers)
			.body(body);

		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
		return jsonNode.get("access_token").asText();
	}
}
