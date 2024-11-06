package com.sparta.deliverybackend.exception.customException;

import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class NotFoundEntityException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public NotFoundEntityException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
