package com.sparta.deliverybackend.api.oauth2.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.sparta.deliverybackend.api.oauth2.adapter.Oauth2Adapter;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2ProviderProperties;
import com.sparta.deliverybackend.api.oauth2.service.Oauth2ClientService;
import com.sparta.deliverybackend.api.oauth2.service.Oauth2ProviderType;
import com.sparta.deliverybackend.api.oauth2.service.RestTemplateOauth2ClientService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(Oauth2Properties.class)
@RequiredArgsConstructor
public class Oauth2Config {

	private final Oauth2Properties properties;
	private final RestTemplate restTemplate;

	@Bean
	public InMemoryProviderRepository inMemoryProviderRepository() {
		Map<Oauth2ProviderType, Oauth2ProviderProperties> providers = Oauth2Adapter.getOauth2Provider(properties);
		return new InMemoryProviderRepository(providers);
	}

	@Bean
	public Oauth2ClientService oauthClientService() {
		return new RestTemplateOauth2ClientService(restTemplate);
	}
}
