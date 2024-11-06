package com.sparta.deliverybackend.domain.restaurant.service;

import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.service.MemberService;
import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.service.OrderService;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.CommentCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.CommentRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Comment;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final MemberService memberService;
	private final OrderService orderService;

	public CommentRespDto createComment(CommentCreateReqDto req, VerifiedMember verifiedMember, Long orderId) {
		Order order = orderService.findOrder(orderId);
		order.validateStatusIsComplete();
		Member member = memberService.findMember(verifiedMember.id());
		order.validateOrderedMember(member);
		Restaurant orderRestaurant = orderService.findOrderRestaurant(orderId);
		Comment comment = Comment.builder()
			.contents(req.content())
			.order(order)
			.member(member)
			.restaurant(orderRestaurant)
			.build();
		Comment savedComment = commentRepository.save(comment);
		return CommentRespDto.from(savedComment);
	}
}
