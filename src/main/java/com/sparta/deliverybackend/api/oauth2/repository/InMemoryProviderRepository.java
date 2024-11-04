package com.sparta.deliverybackend.api.oauth2.repository;

import java.util.HashMap;
import java.util.Map;

import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;

public class InMemoryProviderRepository {
	private final Map<String, Oauth2Provider> providers;

	public InMemoryProviderRepository(Map<String, Oauth2Provider> providers) {
		this.providers = new HashMap<>(providers);
	}

	public Oauth2Provider findProvider(String name) {
		return providers.get(name);
	}
}
