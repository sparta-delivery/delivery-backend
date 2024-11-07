package com.sparta.deliverybackend.api.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.api.auth.controller.dto.LoginReqDto;
import com.sparta.deliverybackend.api.auth.controller.dto.LoginResDto;
import com.sparta.deliverybackend.api.auth.controller.dto.RegisterReqDto;
import com.sparta.deliverybackend.common.JwtHelper;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.exception.customException.AuthCustomException;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;
import com.sparta.deliverybackend.global.security.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final ManagerRepository managerRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtHelper jwtHelper;

	public void register(RegisterReqDto req) {
		if (memberRepository.existsByEmail(req.email())) {
			throw new AuthCustomException(ExceptionCode.DUPLICATED_EMAIL);
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
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
		if (!passwordEncoder.matches(req.password(), member.getPassword())) {
			throw new AuthCustomException(ExceptionCode.NO_MATCH_PASSWORD);
		}
		String accessToken = jwtHelper.generateAccessToken(member);
		return new LoginResDto(accessToken);
	}

	public LoginResDto loginWithOauth(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
		String accessToken = jwtHelper.generateAccessToken(member);
		return new LoginResDto(accessToken);
	}

	public void registerWithOauth(String oauthId, String email, String name) {
		memberRepository.findByEmail(email)
			.ifPresentOrElse(
				savedMember -> savedMember.updateOauthId(oauthId),
				() -> {
					String password = passwordEncoder.encode(UUID.randomUUID().toString());
					Member oauthMember = Member.builder()
						.oauthId(oauthId)
						.email(email)
						.nickname(name)
						.password(password)
						.build();
					memberRepository.save(oauthMember);
				});
	}

	public void managerRegister(RegisterReqDto req) {
		if (managerRepository.existsByEmail(req.email())) {
			throw new AuthCustomException(ExceptionCode.DUPLICATED_EMAIL);
		}
		Manager manager = Manager.builder()
			.nickname(req.nickname())
			.joinPath(req.joinPath())
			.email(req.email())
			.password(passwordEncoder.encode(req.password()))
			.build();
		managerRepository.save(manager);
	}

	public LoginResDto managerLogin(LoginReqDto req) {
		Manager manager = managerRepository.findByEmail(req.email())
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
		if (!passwordEncoder.matches(req.password(), manager.getPassword())) {
			throw new AuthCustomException(ExceptionCode.NO_MATCH_PASSWORD);
		}
		String accessToken = jwtHelper.generateAccessToken(manager);
		return new LoginResDto(accessToken);
	}
}
