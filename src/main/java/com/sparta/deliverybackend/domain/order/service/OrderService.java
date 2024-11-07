package com.sparta.deliverybackend.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
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
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.exception.customException.EtcException;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.customException.NotHaveAuthorityException;
import com.sparta.deliverybackend.exception.customException.OrderPriceMismatchingException;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMenuRepository orderMenuRepository;
	private final MenuRepository menuRepository;
	private final MemberRepository memberRepository;
	private final ManagerRepository managerRepository;

	@Transactional
	public OrderResponseDto createOrder(VerifiedMember verifiedMember, List<OrderMenuDto> orderMenuReqs) {

		if (orderMenuReqs == null || orderMenuReqs.isEmpty()) {
			throw new NotFoundEntityException(ExceptionCode.NOT_FOUND_MENU);
		}

		Member member = memberRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));

		List<Menu> menus = getMenusFromRequest(orderMenuReqs);
		validateSameRestaurant(menus);

		// 최소 주문 금액 확인
		long totalAmount = calculateTotalAmount(orderMenuReqs, menus);
		System.out.println("총액" + totalAmount);

		long minimumOrderAmount = menus.get(0).getRestaurant().getMinPrice();
		if (totalAmount < minimumOrderAmount) {
			throw new OrderPriceMismatchingException(ExceptionCode.NO_SATISFY_MIN_PRICE);
		}

		Order order = Order.builder()
			.orderStatus(OrderStatus.WAIT)
			.member(member)
			.createdAt(LocalDateTime.now())
			.build();

		orderRepository.save(order);

		//----------------------------------------------------

		// 필요한 menuIds를 추출하여 한 번에 조회 (N+1 방지)
		List<Long> menuIds = orderMenuReqs.stream()
			.map(OrderMenuDto::getMenuId)
			.toList();

		Map<Long, Menu> menuMap = menuRepository.findAllById(menuIds).stream()
			.collect(Collectors.toMap(Menu::getId, menu -> menu));

		List<OrderMenu> orderMenus = orderMenuReqs.stream()
			.map(orderMenuReq -> {
				Menu menu = menuMap.get(orderMenuReq.getMenuId());
				if (menu == null) {
					throw new NotFoundEntityException(ExceptionCode.NOT_FOUND_MENU);
				}
				return OrderMenu.builder()
					.menu(menu)
					.order(order)
					.quantity(orderMenuReq.getQuantity())
					.build();
			})
			.toList();

		// 식당 오픈 시간 및 마감 시간 사이에만 주문 가능하도록 로직 추가 예정

		List<OrderMenu> savedOrderMenus = orderMenuRepository.saveAll(orderMenus);
		// jdbc bulkInsert 를 이용하면 좋을 듯, 성능이 5배 늘어 날 것이라고 함.

		//--------------------------------------------------

		return OrderResponseDto.of(order, savedOrderMenus);

	}

	@Transactional
	public OrderUpdateResponseDto updateOrder(VerifiedMember verifiedMember, Long orderId) {

		if (!verifyManager(verifiedMember, orderId)) {
			throw new NotHaveAuthorityException(ExceptionCode.NOT_MANAGER);
		}

		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_ORDER));

		order.updateOrderStatus();
		orderRepository.save(order);

		return OrderUpdateResponseDto.from(order);
	}

	@Transactional
	public OrderCancelResponseDto cancelOrder(VerifiedMember verifiedMember, Long orderId) {
		if (!verifyManager(verifiedMember, orderId) && !verifyCustomer(verifiedMember, orderId)) {
			// 매니저 아이디도, 고객 아이디도 일치하지 않는 경우
			throw new NotHaveAuthorityException(ExceptionCode.NOT_HAVE_AUTHORITY_ORDER_CANCEL);
		}
		Order order = orderRepository.findById(orderId)
			.orElseThrow(
				() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_ORDER));

		order.cancelOrder();
		return OrderCancelResponseDto.from(order);
	}

	public Order findOrder(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_ORDER));
	}

	public Restaurant findOrderRestaurant(Long orderId) {
		OrderMenu orderMenu = orderMenuRepository.findFirstByOrderId(orderId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_ORDER_MENU));
		Menu menu = orderMenu.getMenu();
		return menu.getRestaurant();
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
		if (menus.isEmpty()) {
			throw new NotFoundEntityException(ExceptionCode.NOT_FOUND_MENU);
		}
		Long restaurantId = menus.get(0).getRestaurant().getId();
		boolean allSameRestaurant = menus.stream()
			.allMatch(menu -> menu.getRestaurant().getId().equals(restaurantId));
		if (!allSameRestaurant) {
			throw new EtcException(ExceptionCode.NOT_SAME_RESTAURANT_ORDER);
		}
	}

	// 총 주문 금액 계산
	private long calculateTotalAmount(List<OrderMenuDto> orderMenuReqs, List<Menu> menus) {
		return orderMenuReqs.stream()
			.mapToLong(orderMenuReq -> {
				Menu menu = menus.stream()
					.filter(m -> m.getId().equals(orderMenuReq.getMenuId()))
					.findFirst()
					.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MENU));
				return menu.getPrice() * orderMenuReq.getQuantity();
			}).sum();
	}

	private boolean verifyManager(VerifiedMember verifiedMember, Long orderId) {
		OrderMenu orderMenu = orderMenuRepository.findFirstByOrderId(orderId)
			.orElseThrow(
				() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_ORDER_MENU)
			);

		return managerRepository.findById(verifiedMember.id())
			.map(manager -> manager.equals(orderMenu.getMenu().getRestaurant().getManager()))
			.orElse(false);

		// updateOrderStatus 의 조건문 실행 시 어느 하나가 없는 경우 throw 방지를 위함
	}

	private boolean verifyCustomer(VerifiedMember verifiedMember, Long orderId) {
		OrderMenu orderMenu = orderMenuRepository.findFirstByOrderId(orderId)
			.orElseThrow(
				() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_ORDER_MENU)
			);

		return memberRepository.findById(verifiedMember.id())
			.map(member -> member.equals(orderMenu.getOrder().getMember()))
			.orElse(false);
	}

}
