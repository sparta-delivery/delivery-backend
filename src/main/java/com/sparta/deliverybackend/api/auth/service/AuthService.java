package com.sparta.deliverybackend.api.service;

import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.api.controller.dto.LoginReqDto;
import com.sparta.deliverybackend.api.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.controller.dto.RegisterReqDto;
import com.sparta.deliverybackend.common.JwtHelper;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.global.security.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtHelper jwtHelper;

	public void register(RegisterReqDto req) {
		if (memberRepository.existsByEmail(req.email())) {
			throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
		}
		Member member = Member.builder()
			.nickname(req.nickname())
			.joinPath(req.joinPath())
			.email(req.email())
			.password(passwordEncoder.encode(req.password()))
			.build();
		memberRepository.save(member);
	}

	public LoginResDto login(LoginReqDto req) {
		Member member = memberRepository.findByEmail(req.email())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
		if (!passwordEncoder.matches(req.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		String accessToken = jwtHelper.generateAccessToken(member);
		return new LoginResDto(accessToken);
	}
}
