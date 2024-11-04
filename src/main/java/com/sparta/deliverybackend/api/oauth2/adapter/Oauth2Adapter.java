package com.sparta.deliverybackend.api.oauth2.adapter;

import java.util.HashMap;
import java.util.Map;

import com.sparta.deliverybackend.api.config.Oauth2Properties;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;

public class Oauth2Adapter {

	private Oauth2Adapter() {
	}

	public static Map<String, Oauth2Provider> getOauth2Provider(Oauth2Properties properties) {
		Map<String, Oauth2Provider> providers = new HashMap<>();
		properties.getUser()
			.forEach((key, value) -> providers.put(key, new Oauth2Provider(value, properties.getProvider().get(key))));
		return providers;
	}
}
