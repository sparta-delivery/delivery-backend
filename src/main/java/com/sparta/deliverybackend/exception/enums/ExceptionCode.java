package com.sparta.deliverybackend.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {

	NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당하는 주문을 찾을 수 없습니다."),

	NOT_FOUND_MENU(HttpStatus.NOT_FOUND, "해당하는 메뉴를 찾을 수 없습니다."),

	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당하는 회원을 찾을 수 없습니다."),

	NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND, "해당 식당을 찾을 수 없습니다."),

	NOT_FOUND_ORDER_MENU(HttpStatus.NOT_FOUND, "해당하는 주문_메뉴 정보를 찾을 수 없습니다."),

	NOT_HAVE_AUTHORITY_ORDER_CANCEL(HttpStatus.BAD_REQUEST, "이 주문 조작에 대한 취소 권한이 없습니다."),

	NOT_MANAGER(HttpStatus.BAD_REQUEST, "해당 식당의 메니저가 아닙니다."),

	NOT_SAME_RESTAURANT_ORDER(HttpStatus.BAD_REQUEST, "동일 식당의 메뉴만 주문 가능합니다."),

	NO_SATISFY_MIN_PRICE(HttpStatus.BAD_REQUEST, "최소 주문 금액 미달입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionCode(final HttpStatus httpStatus, final String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

}
