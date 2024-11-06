package com.sparta.deliverybackend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.deliverybackend.exception.customException.EtcException;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.customException.NotHaveAuthorityException;
import com.sparta.deliverybackend.exception.customException.OrderPriceMismatchingException;
import com.sparta.deliverybackend.exception.dto.ExceptionRespDto;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ControllerException")
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundEntityException.class)
	public ResponseEntity<Object> handleNotFoundEntityException(final NotFoundEntityException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionCode(e.getExceptionCode()));
	}

	@ExceptionHandler(OrderPriceMismatchingException.class)
	public ResponseEntity<Object> handleOrderPriceMismatchingException(final OrderPriceMismatchingException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionCode(e.getExceptionCode()));
	}

	@ExceptionHandler(NotHaveAuthorityException.class)
	public ResponseEntity<Object> handleNotHaveAuthorityException(final NotHaveAuthorityException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionCode(e.getExceptionCode()));
	}

	@ExceptionHandler(EtcException.class)
	public ResponseEntity<Object> handleEtcException(final EtcException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionCode(e.getExceptionCode()));
	}

	private ExceptionRespDto makeResponseExceptionCode(ExceptionCode exceptionCode) {
		return ExceptionRespDto.builder()
			.code(exceptionCode.name())
			.message(exceptionCode.getMessage())
			.build();
	}

}
