package com.sparta.deliverybackend.domain.member.entity;

public interface User {
	Long getId();

	String getEmail();

	JoinPath getJoinPath();
}
