package com.sparta.deliverybackend.api.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginReqDto(
	@Email @NotNull(message = "이메일은 필수입니다.") String email,
	@NotNull(message = "비밀번호는 필수입니다.") String password
) {
}
