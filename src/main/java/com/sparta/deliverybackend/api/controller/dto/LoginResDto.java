package com.sparta.deliverybackend.api.controller.dto;

import lombok.Getter;

@Getter
public record LoginResDto(String accessToken) {
}
