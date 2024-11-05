package com.sparta.deliverybackend.api.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliverybackend.api.oauth2.controller.dto.OauthMemberProfile;
import com.sparta.deliverybackend.api.oauth2.provider.Oauth2Provider;

public interface OauthClientService {

	String requestAccessToken(String code, Oauth2Provider provider) throws JsonProcessingException;

	OauthMemberProfile requestMemberProfile(String accessToken, String providerName, Oauth2Provider provider) throws
		JsonProcessingException;
}
