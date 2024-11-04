package com.sparta.deliverybackend.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderMenuDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderMenu;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;
import com.sparta.deliverybackend.domain.order.repository.OrderMenuRepository;
import com.sparta.deliverybackend.domain.order.repository.OrderRepository;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMenuRepository orderMenuRepository;
	private final MenuRepository menuRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public OrderResponseDto createOrder(VerifiedMember verifiedMember, List<OrderMenuDto> orderMenuReqs) {

		if (orderMenuReqs == null || orderMenuReqs.isEmpty()) {
			throw new IllegalArgumentException("주문 메뉴가 없습니다.");
		}

		Member member = memberRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new EntityNotFoundException("Member not found"));

		Order order = Order.builder()
			.orderStatus(OrderStatus.WAIT)
			.member(member)
			.createdAt(LocalDateTime.now())
			.build();

		orderRepository.save(order);

		List<OrderMenu> orderMenus = orderMenuReqs.stream()
			.map(orderMenuReq -> {
				Menu menu = menuRepository.findById(orderMenuReq.getMenuId())
					.orElseThrow(
						() -> new IllegalArgumentException("Invalid menu ID: " + orderMenuReq.getMenuId()));
				OrderMenu orderMenu = OrderMenu.builder()
					.menu(menu)
					.order(order)
					.quantity(orderMenuReq.getQuantity())
					.build();
				return orderMenuRepository.save(orderMenu);

			}).toList();

		return OrderResponseDto.of(order, orderMenus);

	}
}
