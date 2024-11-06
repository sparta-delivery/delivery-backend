package com.sparta.deliverybackend.exception.customException;

import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class EtcException extends RuntimeException {

	private final ExceptionCode exceptionCode;

	public EtcException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getMessage() {
		return exceptionCode.getMessage();
	}
}
