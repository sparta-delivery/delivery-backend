package com.sparta.deliverybackend.api.oauth2.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sparta.deliverybackend.api.oauth2.adapter.Oauth2Adapter;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;

@Configuration
@EnableConfigurationProperties(Oauth2Properties.class)
public class Oauth2Config {

	private final Oauth2Properties properties;

	public Oauth2Config(Oauth2Properties properties) {
		this.properties = properties;
	}

	@Bean
	public InMemoryProviderRepository inMemoryProviderRepository() {
		Map<String, Oauth2Provider> providers = Oauth2Adapter.getOauth2Provider(properties);
		return new InMemoryProviderRepository(providers);
	}
}
