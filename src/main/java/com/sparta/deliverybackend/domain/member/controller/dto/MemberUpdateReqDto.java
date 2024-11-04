package com.sparta.deliverybackend.domain.member.controller.dto;

import jakarta.validation.constraints.NotNull;

public record MemberUpdateReqDto(@NotNull(message = "수정할 닉네임은 필수입니다.") String nickname) {
}
