package com.sparta.deliverybackend.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.MemberUpdateReqDto;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;
	@InjectMocks
	private MemberService memberService;

	// update
	@Test
	@DisplayName("회원 수정 - 존재하지 않는 유저에 대한 수정 요청이 오면 예외가 발생한다.")
	public void update_request_not_exist_member_exception_test() {
		//given
		Long memberId = 1L;
		MemberUpdateReqDto req = new MemberUpdateReqDto("");
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		//when && then
		assertThatThrownBy(() -> memberService.update(memberId, req, verifiedMember))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("존재하지 않는 유저입니다.");
	}

	@Test
	@DisplayName("회원 수정 - 로그인 한 유저와 다른 유저의 수정 요청이 오면 예외가 발생한다.")
	public void has_not_update_authority_exception_test() {
		//given
		Long memberId = 1L;
		MemberUpdateReqDto req = new MemberUpdateReqDto("");
		VerifiedMember verifiedMember = new VerifiedMember(2L);
		Member savedMember = Member.builder()
			.id(1L)
			.build();
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(savedMember));

		//when && then
		assertThatThrownBy(() -> memberService.update(memberId, req, verifiedMember))
			.isInstanceOf(ResponseStatusException.class)
			.hasMessageContaining("권한이 없습니다.");
	}

	@Test
	@DisplayName("회원 수정 - 정상적인 요청이 오면 memberRepository.save()가 한 번 호출된다.")
	public void valid_update_request_success_test() {
		//given
		Long memberId = 1L;
		MemberUpdateReqDto req = new MemberUpdateReqDto("");
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		Member savedMember = Member.builder()
			.id(1L)
			.build();
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(savedMember));

		//when
		memberService.update(memberId, req, verifiedMember);

		//then
		verify(memberRepository, times(1)).save(any());
	}

	// delete
	@Test
	@DisplayName("회원 삭제 - 존재하지 않는 유저에 대한 삭제 요청이 오면 예외가 발생한다.")
	public void delete_request_not_exist_member_exception_test() {
		//given
		Long memberId = 1L;
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		//when && then
		assertThatThrownBy(() -> memberService.delete(memberId, verifiedMember))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("존재하지 않는 유저입니다.");
	}

	@Test
	@DisplayName("회원 삭제 - 로그인 한 유저와 다른 유저의 삭제 요청이 오면 예외가 발생한다.")
	public void has_not_delete_authority_exception_test() {
		//given
		Long memberId = 1L;
		VerifiedMember verifiedMember = new VerifiedMember(2L);
		Member savedMember = Member.builder()
			.id(1L)
			.build();
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(savedMember));

		//when && then
		assertThatThrownBy(() -> memberService.delete(memberId, verifiedMember))
			.isInstanceOf(ResponseStatusException.class)
			.hasMessageContaining("권한이 없습니다.");
	}

	@Test
	@DisplayName("회원 삭제 - 정상적인 요청이 오면 deletedAt이 현재 시간으로 수정된다.")
	public void valid_delete_request_success_test() {
		//given
		Long memberId = 1L;
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		Member savedMember = Member.builder()
			.id(1L)
			.build();
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(savedMember));

		//when
		memberService.delete(memberId, verifiedMember);

		//then
		assertThat(savedMember.getDeletedAt()).isNotNull();
		verify(memberRepository, times(1)).save(any());
	}
}