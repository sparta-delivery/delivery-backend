package com.sparta.deliverybackend.domain.member.entity;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum JoinPath {
	OAUTH("OAUTH"),
	BASIC("BASIC");

	private final String joinPathValue;

	JoinPath(String joinPathValue) {
		this.joinPathValue = joinPathValue;
	}

	@JsonCreator
	public static JoinPath parsing(String inputValue) {
		return Stream.of(JoinPath.values())
			.filter(category -> category.toString().equals(inputValue.toUpperCase()))
			.findFirst()
			.orElse(null);
	}
}
