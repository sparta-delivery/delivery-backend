package com.sparta.deliverybackend.domain.member.service;

import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.MemberUpdateReqDto;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
	}

	public void update(Long id, MemberUpdateReqDto req, VerifiedMember verifiedMember) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
		member.validateAuthority(verifiedMember.id());
		member.update(req);
		memberRepository.save(member);
	}

	public void delete(Long id, VerifiedMember verifiedMember) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
		member.validateAuthority(verifiedMember.id());
		member.delete();
		memberRepository.save(member);
	}
}
