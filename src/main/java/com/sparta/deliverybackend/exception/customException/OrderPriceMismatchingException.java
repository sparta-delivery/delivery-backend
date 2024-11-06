package com.sparta.deliverybackend.exception.customException;

import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class OrderPriceMismatchingException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public OrderPriceMismatchingException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
