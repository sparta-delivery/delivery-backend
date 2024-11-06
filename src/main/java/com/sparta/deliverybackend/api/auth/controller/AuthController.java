package com.sparta.deliverybackend.api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.api.auth.controller.dto.LoginReqDto;
import com.sparta.deliverybackend.api.auth.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.auth.controller.dto.RegisterReqDto;
import com.sparta.deliverybackend.api.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody RegisterReqDto req) {
		authService.register(req);
		return ResponseEntity
			.status(HttpStatus.OK)
			.build();
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto req) {
		LoginResDto response = authService.login(req);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(response);
	}

	@PostMapping("/register/manager")
	public ResponseEntity<Void> managerRegister(@RequestBody RegisterReqDto req) {
		authService.managerRegister(req);
		return ResponseEntity
			.status(HttpStatus.OK)
			.build();
	}

	@PostMapping("/login/manager")
	public ResponseEntity<LoginResDto> managerLogin(@RequestBody LoginReqDto req) {
		LoginResDto response = authService.managerLogin(req);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(response);
	}
}
