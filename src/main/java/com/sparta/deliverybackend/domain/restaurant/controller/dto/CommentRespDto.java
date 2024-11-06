package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import com.sparta.deliverybackend.domain.member.controller.dto.MemberRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRespDto {
	private Long commentId;
	private String content;
	private String reply;
	private MemberRespDto member;

	@Builder
	public CommentRespDto(Long commentId, String content, String reply, MemberRespDto member) {
		this.commentId = commentId;
		this.content = content;
		this.member = member;
		this.reply = reply;
	}

	public static CommentRespDto from(Comment comment) {
		MemberRespDto member = MemberRespDto.from(comment.getMember());
		return CommentRespDto.builder()
			.commentId(comment.getId())
			.content(comment.getContents())
			.member(member)
			.build();
	}
}
