package com.sparta.deliverybackend.exception.customException;

import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter

public class AuthCustomException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public AuthCustomException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
