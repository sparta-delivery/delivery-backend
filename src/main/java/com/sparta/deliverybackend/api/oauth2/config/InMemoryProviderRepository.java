package com.sparta.deliverybackend.api.oauth2.config;

import java.util.HashMap;
import java.util.Map;

import com.sparta.deliverybackend.api.oauth2.provider.Oauth2ProviderProperties;
import com.sparta.deliverybackend.api.oauth2.service.Oauth2ProviderType;

public class InMemoryProviderRepository {
	private final Map<Oauth2ProviderType, Oauth2ProviderProperties> providers;

	public InMemoryProviderRepository(Map<Oauth2ProviderType, Oauth2ProviderProperties> providers) {
		this.providers = new HashMap<>(providers);
	}

	public Oauth2ProviderProperties findProvider(Oauth2ProviderType name) {
		return providers.get(name);
	}
}
