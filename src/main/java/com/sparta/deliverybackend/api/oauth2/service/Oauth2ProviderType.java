package com.sparta.deliverybackend.api.oauth2.service;

public enum Oauth2ProviderType {
	GOOGLE("google"),
	KAKAO("kakao"),
	GITHUB("github"),
	NAVER("naver");

	private final String providerType;

	Oauth2ProviderType(String providerType) {
		this.providerType = providerType;
	}

	public static Oauth2ProviderType of(String providerName) {
		for (Oauth2ProviderType providerType : Oauth2ProviderType.values()) {
			if (providerType.providerType.equals(providerName)) {
				return providerType;
			}
		}
		throw new IllegalArgumentException("제공하지 않는 Provider입니다.");
	}
}
