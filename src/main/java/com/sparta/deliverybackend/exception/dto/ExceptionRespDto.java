package com.sparta.deliverybackend.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionRespDto {

	private String code;
	private String message;
}
