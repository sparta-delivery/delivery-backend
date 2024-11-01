package com.sparta.deliverybackend.domain.member.entity;

public enum JoinPath {
	KAKAO("KAKAO"),
	BASIC("BASIC");

	private String joinPathValue;

	JoinPath(String joinPathValue) {
		this.joinPathValue = joinPathValue;
	}
}
