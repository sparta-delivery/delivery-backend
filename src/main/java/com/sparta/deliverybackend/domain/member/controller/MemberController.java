package com.sparta.deliverybackend.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.MemberUpdateReqDto;
import com.sparta.deliverybackend.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PatchMapping("/{id}")
	public ResponseEntity<Void> update(
		@PathVariable Long id,
		@RequestBody MemberUpdateReqDto req,
		VerifiedMember verifiedMember
	) {
		memberService.update(id, req, verifiedMember);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, VerifiedMember verifiedMember) {
		memberService.delete(id, verifiedMember);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
