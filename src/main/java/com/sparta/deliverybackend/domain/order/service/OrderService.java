package com.sparta.deliverybackend.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderCancelResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderMenuDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderUpdateResponseDto;
import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderMenu;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;
import com.sparta.deliverybackend.domain.order.repository.OrderMenuRepository;
import com.sparta.deliverybackend.domain.order.repository.OrderRepository;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMenuRepository orderMenuRepository;
	private final MenuRepository menuRepository;
	private final MemberRepository memberRepository;
	private final ManagerRepository managerRepository;
	private final RestaurantRepository restaurantRepository;

	@Transactional
	public OrderResponseDto createOrder(VerifiedMember verifiedMember, List<OrderMenuDto> orderMenuReqs) {

		if (orderMenuReqs == null || orderMenuReqs.isEmpty()) {
			throw new IllegalArgumentException("주문 메뉴가 없습니다.");
		}

		Member member = memberRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new EntityNotFoundException("Member not found"));

		List<Menu> menus = getMenusFromRequest(orderMenuReqs);
		validateSameRestaurant(menus);

		// 최소 주문 금액 확인
		long totalAmount = calculateTotalAmount(orderMenuReqs, menus);
		System.out.println("총액" + totalAmount);

		long minimumOrderAmount = menus.get(0).getRestaurant().getMinPrice();
		if (totalAmount < minimumOrderAmount) {
			throw new IllegalArgumentException("최소 주문 금액을 충족하지 못했습니다. 최소 주문 금액: " + minimumOrderAmount + "원");
		}

		Order order = Order.builder()
			.orderStatus(OrderStatus.WAIT)
			.member(member)
			.createdAt(LocalDateTime.now())
			.build();

		orderRepository.save(order);

		// N+1 문제 해결 필요
		List<OrderMenu> orderMenus = orderMenuReqs.stream()
			.map(orderMenuReq -> {
				Menu menu = menuRepository.findById(orderMenuReq.getMenuId())
					.orElseThrow(
						() -> new IllegalArgumentException("Invalid menu ID: " + orderMenuReq.getMenuId()));
				return OrderMenu.builder()
					.menu(menu)
					.order(order)
					.quantity(orderMenuReq.getQuantity())
					.build();

			}).toList();

		List<OrderMenu> savedOrderMenus = orderMenuRepository.saveAll(orderMenus);
		// jdbc bulkInsert 를 이용하면 좋을 듯, 성능이 5배 늘어 날 것이라고 함.

		return OrderResponseDto.of(order, savedOrderMenus);

	}

	@Transactional
	public OrderUpdateResponseDto updateOrderStatus(VerifiedMember verifiedMember, Long orderId) {

		if (!verifyManager(verifiedMember, orderId)) {
			throw new IllegalArgumentException("해당 식당의 매니저가 아닙니다.");
		}

		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new EntityNotFoundException("Order id " + orderId + " not found"));

		order.updateOrderStatus();
		orderRepository.save(order);
		// 알람 기능 필요

		return OrderUpdateResponseDto.from(order);
	}

	@Transactional
	public OrderCancelResponseDto cancelOrder(VerifiedMember verifiedMember, Long orderId) {
		if (!verifyManager(verifiedMember, orderId) && !verifyCustomer(verifiedMember, orderId)) {
			// 매니저 아이디도, 고객 아이디도 일치하지 않는 경우
			throw new IllegalArgumentException("이 주문을 취소할 권한이 없습니다.");
		}
		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new EntityNotFoundException("Order id " + orderId + " not found"));

		order.cancelOrder();
		return OrderCancelResponseDto.from(order);
	}

	// 주문에 포함된 메뉴들 조회
	private List<Menu> getMenusFromRequest(List<OrderMenuDto> orderMenuReqs) {
		List<Long> menuIds = orderMenuReqs.stream()
			.map(OrderMenuDto::getMenuId)
			.toList();
		return menuRepository.findAllById(menuIds);
	}

	// 동일한 식당의 메뉴인지 검증
	private void validateSameRestaurant(List<Menu> menus) {
		Long restaurantId = menus.get(0).getRestaurant().getId();
		boolean allSameRestaurant = menus.stream()
			.allMatch(menu -> menu.getRestaurant().getId().equals(restaurantId));
		if (!allSameRestaurant) {
			throw new IllegalArgumentException("같은 식당에서만 주문이 가능합니다.");
		}
	}

	// 총 주문 금액 계산
	private long calculateTotalAmount(List<OrderMenuDto> orderMenuReqs, List<Menu> menus) {
		return orderMenuReqs.stream()
			.mapToLong(orderMenuReq -> {
				Menu menu = menus.stream()
					.filter(m -> m.getId().equals(orderMenuReq.getMenuId()))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Invalid menu ID: " + orderMenuReq.getMenuId()));
				return menu.getPrice() * orderMenuReq.getQuantity();
			}).sum();
	}

	private boolean verifyManager(VerifiedMember verifiedMember, Long orderId) {
		OrderMenu orderMenu = orderMenuRepository.findFirstByOrderId(orderId)
			.orElseThrow(
				() -> new IllegalArgumentException("해당 주문 id 에 해당하는 메뉴가 없습니다.")
			);

		Manager manager = managerRepository.findById(verifiedMember.id()).orElseThrow(
			() -> new IllegalArgumentException("해당하는 식당 매니저를 찾을 수 없습니다.")
		);

		return manager.equals(orderMenu.getMenu().getRestaurant().getManager());
	}

	private boolean verifyCustomer(VerifiedMember verifiedMember, Long orderId) {
		OrderMenu orderMenu = orderMenuRepository.findFirstByOrderId(orderId)
			.orElseThrow(
				() -> new IllegalArgumentException("해당 주문 id 에 해당하는 메뉴가 없습니다.")
			);

		Member member = memberRepository.findById(verifiedMember.id()).orElseThrow(
			() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다.")
		);

		return member.equals(orderMenu.getOrder().getMember());
	}

}
