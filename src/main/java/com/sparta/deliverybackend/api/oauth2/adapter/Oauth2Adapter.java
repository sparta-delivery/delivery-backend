package com.sparta.deliverybackend.api.oauth2.adapter;

import java.util.HashMap;
import java.util.Map;

import com.sparta.deliverybackend.api.oauth2.config.Oauth2Properties;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2ProviderProperties;
import com.sparta.deliverybackend.api.oauth2.service.Oauth2ProviderType;

public class Oauth2Adapter {

	private Oauth2Adapter() {
	}

	public static Map<Oauth2ProviderType, Oauth2ProviderProperties> getOauth2Provider(Oauth2Properties properties) {
		Map<Oauth2ProviderType, Oauth2ProviderProperties> providers = new HashMap<>();
		properties.getUser().forEach((key, value) -> {
			Oauth2ProviderType providerType = Oauth2ProviderType.of(key);
			providers.put(providerType, new Oauth2ProviderProperties(value, properties.getProvider().get(key)));
		});
		return providers;
	}
}
