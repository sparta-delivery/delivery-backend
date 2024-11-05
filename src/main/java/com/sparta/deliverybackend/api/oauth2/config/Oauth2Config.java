package com.sparta.deliverybackend.api.oauth2.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.sparta.deliverybackend.api.oauth2.adapter.Oauth2Adapter;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;
import com.sparta.deliverybackend.api.oauth2.service.OauthClientService;
import com.sparta.deliverybackend.api.oauth2.service.RestTemplateOauthClientService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(Oauth2Properties.class)
@RequiredArgsConstructor
public class Oauth2Config {

	private final Oauth2Properties properties;
	private final RestTemplate restTemplate;
	private final OauthClientService oauthClientService;

	@Bean
	public InMemoryProviderRepository inMemoryProviderRepository() {
		Map<String, Oauth2Provider> providers = Oauth2Adapter.getOauth2Provider(properties);
		return new InMemoryProviderRepository(providers);
	}

	@Bean
	public OauthClientService oauthClientService() {
		return new RestTemplateOauthClientService(restTemplate);
	}
}
