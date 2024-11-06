package com.sparta.deliverybackend.domain.restaurant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.CommentCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.CommentRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.CommentUpdateReqDto;
import com.sparta.deliverybackend.domain.restaurant.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/{orderId}/comments")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommentRespDto> createComment(
		@PathVariable Long orderId,
		@RequestBody CommentCreateReqDto req,
		VerifiedMember verifiedMember
	) {
		CommentRespDto response = commentService.createComment(req, verifiedMember, orderId);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(response);
	}

	@PostMapping("/{commentId}/manager-comment")
	public ResponseEntity<CommentRespDto> createManagerComment(
		@PathVariable Long commentId,
		@RequestBody CommentCreateReqDto req,
		VerifiedMember verifiedMember
	) {
		CommentRespDto response = commentService.createManagerComment(req, verifiedMember, commentId);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(response);
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<Void> updateComment(
		@PathVariable Long orderId,
		@PathVariable Long commentId,
		@RequestBody CommentUpdateReqDto req,
		VerifiedMember verifiedMember
	) {
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@PathVariable Long orderId,
		@PathVariable Long commentId,
		VerifiedMember verifiedMember
	) {
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
