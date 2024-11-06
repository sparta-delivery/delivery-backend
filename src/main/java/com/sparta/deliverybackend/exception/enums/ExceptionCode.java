package com.sparta.deliverybackend.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {
	INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),

	NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당하는 주문을 찾을 수 없습니다."),

	NOT_FOUND_MENU(HttpStatus.NOT_FOUND, "해당하는 메뉴를 찾을 수 없습니다."),

	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당하는 회원을 찾을 수 없습니다."),

	NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND, "해당 식당을 찾을 수 없습니다."),

	NOT_FOUND_AD(HttpStatus.NOT_FOUND, "해당 광고를 찾을 수 없습니다."),

	NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),

	NOT_FOUND_MANAGER(HttpStatus.NOT_FOUND, "해당 사장을 찾을 수 없습니다."),

	NOT_FOUND_ORDER_MENU(HttpStatus.NOT_FOUND, "해당하는 주문_메뉴 정보를 찾을 수 없습니다."),

	NOT_FOUND_FAVORITE(HttpStatus.NOT_FOUND, "해당하는 즐겨찾기 정보를 찾을 수 없습니다."),

	NOT_HAVE_AUTHORITY_ORDER_CANCEL(HttpStatus.UNAUTHORIZED, "이 주문 조작에 대한 취소 권한이 없습니다."),

	NOT_HAVE_AUTHORITY_CREATE_MENU(HttpStatus.UNAUTHORIZED, "메뉴 생성 권한이 없습니다."),

	NOT_HAVE_AUTHORITY_AD_CREATE(HttpStatus.UNAUTHORIZED, "광고 생성에 대한 권한이 없습니다."),

	NOT_HAVE_AUTHORITY_MEMBER(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다."),

	NOT_MANAGER(HttpStatus.UNAUTHORIZED, "해당 식당의 메니저가 아닙니다."),

	TOO_MUCH_RESTAURANT(HttpStatus.BAD_REQUEST, "최대 3개 까지만 식당을 생성 할 수 있습니다."),

	NOT_MATCH_RESTAURANT_MENU(HttpStatus.BAD_REQUEST, "해당 식당의 메뉴가 아닙니다."),

	NOT_SAME_RESTAURANT_ORDER(HttpStatus.BAD_REQUEST, "동일 식당의 메뉴만 주문 가능합니다."),

	NO_SATISFY_MIN_PRICE(HttpStatus.BAD_REQUEST, "최소 주문 금액 미달입니다."),

	NO_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

	DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일 입니다."),

	NO_COMPLETED_ORDER(HttpStatus.BAD_REQUEST, "완료된 주문이 아닙니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionCode(final HttpStatus httpStatus, final String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

}
