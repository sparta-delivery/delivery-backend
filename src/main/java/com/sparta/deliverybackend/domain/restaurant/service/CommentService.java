package com.sparta.deliverybackend.domain.restaurant.service;

import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
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
	private final ManagerRepository managerRepository;
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

	public CommentRespDto createManagerComment(CommentCreateReqDto req, VerifiedMember verifiedMember, Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사장입니다."));
		comment.getRestaurant().validateRestaurantManager(manager);
		comment.updateManagerReply(req.content());
		return CommentRespDto.from(comment);
	}

	public void deleteComment(VerifiedMember verifiedMember, Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
		Member member = memberService.findMember(verifiedMember.id());
		comment.validateMember(member);

		comment.delete();
		commentRepository.save(comment);
	}
}
