package com.sparta.deliverybackend.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody RegisterReqDto req) {

		return ResponseEntity
			.status(HttpStatus.OK)
			.build();
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto req) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.build(authService.login(req));
	}
}
