package com.sparta.deliverybackend.api.oauth2.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthLoginResponseDto {
	private String oauthId;
	private String email;
	private String name;
	private String accessToken;
}
