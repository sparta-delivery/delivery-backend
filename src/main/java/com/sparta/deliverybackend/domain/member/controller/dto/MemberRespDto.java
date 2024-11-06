package com.sparta.deliverybackend.domain.member.controller.dto;

import com.sparta.deliverybackend.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRespDto {

	private Long id;
	private String nickName;
	private String email;

	@Builder
	public MemberRespDto(Long id, String nickName, String email) {
		this.id = id;
		this.nickName = nickName;
		this.email = email;
	}

	public static MemberRespDto from(Member member) {
		return MemberRespDto.builder()
			.id(member.getId())
			.nickName(member.getNickname())
			.email(member.getEmail())
			.build();
	}
}
