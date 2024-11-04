package com.sparta.deliverybackend.api.controller.dto;

import com.sparta.deliverybackend.domain.member.entity.JoinPath;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterReqDto(
	@NotNull(message = "닉네임은 필수입니다.") String nickname,
	@Email @NotNull(message = "이메일은 필수입니다.") String email,
	@NotNull(message = "가입 경로는 필수입니다.") JoinPath joinPath,
	@NotNull(message = "비밀번호는 필수입니다.") String password
) {
}
