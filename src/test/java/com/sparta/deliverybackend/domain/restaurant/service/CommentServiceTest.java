package com.sparta.deliverybackend.domain.restaurant.service;

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

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.member.service.MemberService;
import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;
import com.sparta.deliverybackend.domain.order.service.OrderService;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.CommentCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.CommentRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Comment;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private MemberService memberService;

	@Mock
	private OrderService orderService;

	@Mock
	private ManagerRepository managerRepository;

	@InjectMocks
	private CommentService commentService;

	@Test
	@DisplayName("정상적인 댓글 작성 성공 테스트")
	public void create_comment_success_test() {
		//given
		CommentCreateReqDto req = new CommentCreateReqDto("comments");
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		Long orderId = 1L;
		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.build();
		Member member = Member.builder()
			.id(verifiedMember.id())
			.build();
		Order order = Order.builder()
			.id(orderId)
			.member(member)
			.orderStatus(OrderStatus.COMPLETE)
			.build();
		Comment comment = Comment.builder()
			.id(1L)
			.member(member)
			.restaurant(restaurant)
			.order(order)
			.contents(req.content())
			.build();

		when(orderService.findOrder(orderId)).thenReturn(order);
		when(memberService.findMember(verifiedMember.id())).thenReturn(member);
		when(orderService.findOrderRestaurant(orderId)).thenReturn(restaurant);
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);
		//when
		CommentRespDto commentRespDto = commentService.createComment(req, verifiedMember, orderId);

		//then
		verify(commentRepository, times(1)).save(any(Comment.class));
		assertThat(commentRespDto.getCommentId()).isEqualTo(member.getId());
		assertThat(commentRespDto.getContent()).isEqualTo("comments");
	}

	@Test
	@DisplayName("주문이 완료가 안됐을 때 리뷰 작성을하면 예외가 발생한다.")
	public void create_comment_not_complete_exception_test() {
		//given
		CommentCreateReqDto req = new CommentCreateReqDto("comments");
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		Long orderId = 1L;
		Member member = Member.builder()
			.id(verifiedMember.id())
			.build();
		Order order = Order.builder()
			.id(orderId)
			.member(member)
			.orderStatus(OrderStatus.WAIT)
			.build();

		when(orderService.findOrder(orderId)).thenReturn(order);

		//when && then
		assertThatThrownBy(() -> commentService.createComment(req, verifiedMember, orderId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("완료된 주문이 아닙니다.");
	}

	@Test
	@DisplayName("주문한 사람이 아닌 다른 사람이 리뷰 작성을하면 예외가 발생한다.")
	public void create_comment_not_order_member_exception_test() {
		//given
		CommentCreateReqDto req = new CommentCreateReqDto("comments");
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		Long orderId = 1L;
		Member member = Member.builder()
			.id(verifiedMember.id())
			.build();
		Member otherMember = Member.builder()
			.id(3L)
			.build();
		Order order = Order.builder()
			.id(orderId)
			.member(otherMember)
			.orderStatus(OrderStatus.COMPLETE)
			.build();

		when(orderService.findOrder(orderId)).thenReturn(order);
		when(memberService.findMember(verifiedMember.id())).thenReturn(member);

		//when && then
		assertThatThrownBy(() -> commentService.createComment(req, verifiedMember, orderId))
			.isInstanceOf(ResponseStatusException.class)
			.hasMessageContaining("권한이 없습니다.");
	}

	@Test
	@DisplayName("사장 답글 정상 생성 테스트")
	public void create_manager_comment_success_test() {
		// given
		Long orderId = 1L;
		CommentCreateReqDto req = new CommentCreateReqDto("comments");
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		Manager manager = Manager.builder()
			.id(verifiedMember.id())
			.build();
		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.manager(manager)
			.build();
		Member member = Member.builder()
			.id(2L)
			.build();
		Order order = Order.builder()
			.id(orderId)
			.member(member)
			.orderStatus(OrderStatus.COMPLETE)
			.build();
		Comment comment = Comment.builder()
			.id(1L)
			.member(member)
			.restaurant(restaurant)
			.order(order)
			.contents(req.content())
			.build();

		when(managerRepository.findById(any())).thenReturn(Optional.of(manager));
		when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

		//when
		CommentRespDto managerComment = commentService.createManagerComment(req, verifiedMember, comment.getId());

		//then
		assertThat(managerComment.getReply()).isEqualTo("comments");
	}

	@Test
	@DisplayName("사장 답글 작성 시 리뷰가 없으면 예외가 발생한다")
	public void create_manager_comment_exception_test() {
		// given
		CommentCreateReqDto req = new CommentCreateReqDto("comments");
		VerifiedMember verifiedMember = new VerifiedMember(1L);
		when(commentRepository.findById(any())).thenReturn(Optional.empty());

		//when
		assertThatThrownBy(() -> commentService.createManagerComment(req, verifiedMember, 1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("존재하지 않는 리뷰입니다.");
	}

	@Test
	@DisplayName("존재하지 않는 사장이면 예외발생")
	public void not_exist_manager_create_comment_exception_test() {
		// given
		CommentCreateReqDto req = new CommentCreateReqDto("comments");
		VerifiedMember verifiedMember = new VerifiedMember(1L);

		when(commentRepository.findById(any())).thenReturn(Optional.of(new Comment()));
		when(managerRepository.findById(any())).thenReturn(Optional.empty());

		//when
		assertThatThrownBy(() -> commentService.createManagerComment(req, verifiedMember, 1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("존재하지 않는 사장입니다.");
	}
}