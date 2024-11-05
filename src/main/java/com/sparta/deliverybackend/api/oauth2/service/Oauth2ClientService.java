package com.sparta.deliverybackend.api.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2ProviderProperties;

public interface Oauth2ClientService {

	String requestAccessToken(String code, Oauth2ProviderProperties provider) throws JsonProcessingException;

	OauthMemberProfile requestMemberProfile(String accessToken, String providerName,
		Oauth2ProviderProperties provider) throws
		JsonProcessingException;
}
