package com.sparta.deliverybackend.exception.customException;

import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class NotHaveAuthorityException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public NotHaveAuthorityException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
