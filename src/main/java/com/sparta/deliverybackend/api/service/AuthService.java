package com.sparta.deliverybackend.api.service;

import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.api.controller.dto.LoginReqDto;
import com.sparta.deliverybackend.api.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.controller.dto.RegisterReqDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	public void register(RegisterReqDto req) {
	}

	public LoginResDto login(LoginReqDto req) {
		return new LoginResDto("");
	}
}
