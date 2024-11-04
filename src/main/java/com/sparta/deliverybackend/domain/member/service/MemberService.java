package com.sparta.deliverybackend.domain.member.service;

import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.MemberUpdateReqDto;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public void update(Long id, MemberUpdateReqDto req, VerifiedMember verifiedMember) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
		member.validateAuthority(verifiedMember.id());
		member.update(req);
		memberRepository.save(member);
	}

	public void delete(Long id, VerifiedMember verifiedMember) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
		member.validateAuthority(verifiedMember.id());
		member.delete();
		memberRepository.save(member);
	}
}
