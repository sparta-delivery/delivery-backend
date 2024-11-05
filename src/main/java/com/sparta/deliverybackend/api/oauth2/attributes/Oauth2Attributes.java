package com.sparta.deliverybackend.api.oauth2.attributes;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;

public enum Oauth2Attributes {
	GITHUB("github") {
		@Override
		public OauthMemberProfile of(JsonNode responseJsonBody) {
			return OauthMemberProfile.builder()
				.oauthId(responseJsonBody.get("id").asText())
				.email(responseJsonBody.get("email").asText())
				.name(responseJsonBody.get("name").asText())
				.build();
		}
	},
	NAVER("naver") {
		@Override
		public OauthMemberProfile of(JsonNode responseJsonBody) {
			return OauthMemberProfile.builder()
				.oauthId(responseJsonBody.get("id").asText())
				.email(responseJsonBody.get("email").asText())
				.name(responseJsonBody.get("name").asText())
				.build();
		}
	},
	GOOGLE("google") {
		@Override
		public OauthMemberProfile of(JsonNode responseJsonBody) {
			return OauthMemberProfile.builder()
				.oauthId(responseJsonBody.get("sub").asText())
				.email(responseJsonBody.get("email").asText())
				.name(responseJsonBody.get("name").asText())
				.build();
		}
	};

	private final String providerName;

	Oauth2Attributes(String name) {
		this.providerName = name;
	}

	public static OauthMemberProfile extract(String providerName, JsonNode responseJsonBody) {
		return Arrays.stream(values())
			.filter(provider -> providerName.equals(provider.providerName))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new)
			.of(responseJsonBody);
	}

	public abstract OauthMemberProfile of(JsonNode responseJsonBody);
}