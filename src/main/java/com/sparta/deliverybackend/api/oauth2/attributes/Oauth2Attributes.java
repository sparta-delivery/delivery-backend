package com.sparta.deliverybackend.api.oauth2.attributes;

import java.util.Arrays;
import java.util.Map;

import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;

public enum Oauth2Attributes {
	GITHUB("github") {
		@Override
		public OauthMemberProfile of(Map<String, Object> attributes) {
			return OauthMemberProfile.builder()
				.oauthId((String)attributes.get("id"))
				.email((String)attributes.get("email"))
				.name((String)attributes.get("name"))
				.build();
		}
	},
	KAKAO("kakao") {
		@Override
		public OauthMemberProfile of(Map<String, Object> attributes) {
			Map<String, Object> containEmailResponse = ((Map<String, Object>)attributes.get(
				"kakao_account"));
			Map<String, Object> containNameResponse = ((Map<String, Object>)attributes.get(
				"properties"));
			return OauthMemberProfile.builder()
				.oauthId(attributes.get("id").toString())
				.email((String)containEmailResponse.get("email"))
				.name((String)containNameResponse.get("nickname"))
				.build();
		}
	},
	NAVER("naver") {
		@Override
		public OauthMemberProfile of(Map<String, Object> attributes) {
			Map<String, Object> response = (Map<String, Object>)attributes.get("response");
			return OauthMemberProfile.builder()
				.oauthId((String)response.get("id"))
				.email((String)response.get("email"))
				.name((String)response.get("name"))
				.build();
		}
	},
	GOOGLE("google") {
		@Override
		public OauthMemberProfile of(Map<String, Object> attributes) {
			return OauthMemberProfile.builder()
				.oauthId((String)attributes.get("id"))
				.email((String)attributes.get("email"))
				.name((String)attributes.get("name"))
				.build();
		}
	};

	private final String providerName;

	Oauth2Attributes(String name) {
		this.providerName = name;
	}

	public static OauthMemberProfile extract(String providerName, Map<String, Object> attributes) {
		System.out.println("===============================");
		System.out.println(providerName);
		System.out.println("===============================");
		return Arrays.stream(values())
			.filter(provider -> providerName.equals(provider.providerName))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new)
			.of(attributes);
	}

	public abstract OauthMemberProfile of(Map<String, Object> attributes);
}