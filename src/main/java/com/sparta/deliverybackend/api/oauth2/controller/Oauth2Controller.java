package com.sparta.deliverybackend.api.oauth2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.deliverybackend.api.auth.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.oauth2.service.Oauth2Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class Oauth2Controller {

	private final Oauth2Service oauth2Service;

	@GetMapping("/login/{provider}")
	public ResponseEntity<LoginResDto> login(
		@PathVariable String provider,
		@RequestParam String code
	) throws JsonProcessingException {
		LoginResDto response = oauth2Service.loginOrRegister(provider, code);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(response);
	}
}