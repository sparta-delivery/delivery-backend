package com.sparta.deliverybackend.domain.member.entity;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.deliverybackend.domain.BaseTimeStampEntity;
import com.sparta.deliverybackend.domain.member.controller.dto.MemberUpdateReqDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeStampEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(name = "join_path", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private JoinPath joinPath;

	@Column
	private LocalDateTime deletedAt;

	public void validateAuthority(Long id) {
		if (!this.id.equals(id)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
	}

	public void update(MemberUpdateReqDto req) {
		nickname = req.nickname();
	}
}
