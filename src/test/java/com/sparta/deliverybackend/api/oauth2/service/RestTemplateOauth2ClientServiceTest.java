package com.sparta.deliverybackend.api.oauth2.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2ProviderProperties;

@ExtendWith(MockitoExtension.class)
class RestTemplateOauth2ClientServiceTest {

	@Mock
	RestTemplate restTemplate;

	@InjectMocks
	RestTemplateOauth2ClientService restTemplateOauth2ClientService;

	Oauth2ProviderProperties providerProperties;

	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		providerProperties = Oauth2ProviderProperties.builder()
			.clientId("cliendId")
			.clientSecret("secret")
			.redirectUrl("redirectUrl")
			.tokenUrl("https://example.com/token")
			.userInfoUrl("userInfoUrl")
			.build();
	}

	@Test
	@DisplayName("requestAccessToken 정상 동작")
	public void requestAccessToken_success_test() throws JsonProcessingException {
		//given
		String authorizationCode = "authorizationCode";
		String accessToken = "accessToken";

		JsonNode mockResponse = mapper.createObjectNode().put("access_token", accessToken);
		ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse.toString(), HttpStatus.OK);
		when(restTemplate.exchange(any(RequestEntity.class), eq(String.class)))
			.thenReturn(responseEntity);

		//when
		String responseToken = restTemplateOauth2ClientService
			.requestAccessToken(authorizationCode, providerProperties);

		//then
		assertThat(responseToken).isEqualTo(accessToken);
	}

	@Test
	@DisplayName("requestMemberProfile 정상 동작")
	public void requestMemberProfile_success_test() throws JsonProcessingException {
		//given
		String accessToken = "oauthAccessToken";
		String providerName = "github";

		Map<String, Object> mockAttributes = Map.of("id", "12345", "name", "Test User", "email", "test@email.com");
		String mockResponseJson = new ObjectMapper().writeValueAsString(mockAttributes);
		ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
		when(restTemplate.exchange(
			eq(providerProperties.getUserInfoUrl()),
			eq(HttpMethod.GET),
			any(HttpEntity.class),
			eq(String.class)
		)).thenReturn(responseEntity);

		//when
		OauthMemberProfile memberProfile = restTemplateOauth2ClientService.requestMemberProfile(accessToken,
			providerName, providerProperties);

		//then
		assertThat(memberProfile.getOauthId()).isEqualTo("12345");
		assertThat(memberProfile.getName()).isEqualTo("Test User");
		assertThat(memberProfile.getEmail()).isEqualTo("test@email.com");
	}

	@Test
	@DisplayName("잘못된 경로로 요청하면 RestClientException이 발생한다.")
	public void requestAccessToken_exception_test() {
		//given
		String authorizationCode = "authorizationCode";
		when(restTemplate.exchange(any(RequestEntity.class), eq(String.class)))
			.thenThrow(new RestClientException("RestClientException"));

		//when && then
		assertThatThrownBy(
			() -> restTemplateOauth2ClientService.requestAccessToken(authorizationCode, providerProperties))
			.isInstanceOf(RestClientException.class)
			.hasMessageContaining("RestClientException");
	}
}