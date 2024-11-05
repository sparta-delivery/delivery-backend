package com.sparta.deliverybackend.domain.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.JoinPath;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderMenuDto;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderResponseDto;
import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;
import com.sparta.deliverybackend.domain.order.repository.OrderMenuRepository;
import com.sparta.deliverybackend.domain.order.repository.OrderRepository;
import com.sparta.deliverybackend.domain.restaurant.entity.CuisineType;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderMenuRepository orderMenuRepository;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	@DisplayName("회원 배달 주문 시 주문 생성 테스트")
	void createOrderTest() {
		// given
		Long memberId = 1L;

		// 임시 멤버 객체 생성
		Member member = Member.builder()
			.id(memberId)
			.nickname("태우")
			.email("wootaepark@naver.com")
			.password("1234")
			.joinPath(JoinPath.BASIC)
			.build();

		// 임시 식당 객체 생성
		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.minPrice(15000) // 최소 주문 금액 설정
			.build();

		// 임시 메뉴 객체 생성
		Menu menu = Menu.builder()
			.id(1L)
			.name("떡볶이")
			.description("맛있어요")
			.cuisineType(CuisineType.KOREAN)
			.restaurant(restaurant)
			.price(10000)
			.build();

		OrderMenuDto orderMenuDto = OrderMenuDto.builder()
			.menuId(menu.getId())
			.quantity(2L)
			.build();

		// Mocking
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		when(menuRepository.findAllById(anyList())).thenReturn(List.of(menu));

		Order order = Order.builder()
			.id(1L)
			.orderStatus(OrderStatus.WAIT)
			.member(member)
			.createdAt(LocalDateTime.now())
			.build();

		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(orderMenuRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		OrderResponseDto orderResponseDto = orderService.createOrder(new VerifiedMember(1L),
			List.of(orderMenuDto));

		// then
		assertNotNull(orderResponseDto);
		assertEquals("태우", orderResponseDto.getCustomer());
		assertEquals(menu.getId(), orderResponseDto.getRequestMenus().get(0).getMenuId());
		assertEquals(2L, orderResponseDto.getRequestMenus().get(0).getQuantity());
		assertEquals(OrderStatus.WAIT, orderResponseDto.getOrderStatus());
	}

	@Test
	@DisplayName("주문 생성 시 메뉴가 없을 경우 예외 발생 테스트")
	void createOrder_NoMenus() {
		// when & then
		assertThrows(IllegalArgumentException.class, () -> {
			orderService.createOrder(new VerifiedMember(1L), null);
		});
	}

	@Test
	@DisplayName("주문 생성 시 최소 주문 금액 미달 예외 발생 테스트")
	void createOrder_MinimumAmountNotMet() {
		// given
		Long memberId = 1L;

		// 임시 멤버 객체 생성
		Member member = Member.builder()
			.id(memberId)
			.nickname("태우")
			.email("wootaepark@naver.com")
			.password("1234")
			.joinPath(JoinPath.OAUTH)
			.build();

		// 임시 식당 객체 생성
		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.minPrice(15000) // 최소 주문 금액 설정
			.build();

		// 임시 메뉴 객체 생성
		Menu menu = Menu.builder()
			.id(1L)
			.name("떡볶이")
			.description("맛있어요")
			.cuisineType(CuisineType.KOREAN)
			.restaurant(restaurant)
			.price(10000)
			.build();

		OrderMenuDto orderMenuDto = OrderMenuDto.builder()
			.menuId(menu.getId())
			.quantity(1L) // 총액이 최소 금액 미달
			.build();

		// Mocking
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		when(menuRepository.findAllById(anyList())).thenReturn(List.of(menu));

		// when & then
		assertThrows(IllegalArgumentException.class, () -> {
			orderService.createOrder(new VerifiedMember(memberId), List.of(orderMenuDto));
		});
	}

	@Test
	@DisplayName("주문 생성 시 동일 식당이 아닐 경우 예외 발생 테스트")
	void createOrder_DifferentRestaurants() {
		// given
		Long memberId = 1L;

		// 임시 멤버 객체 생성
		Member member = Member.builder()
			.id(memberId)
			.nickname("태우")
			.email("wootaepark@naver.com")
			.password("1234")
			.joinPath(JoinPath.OAUTH)
			.build();

		// 임시 식당 객체 생성 (식당 1)
		Restaurant restaurant1 = Restaurant.builder()
			.id(1L)
			.minPrice(15000)
			.build();

		// 임시 메뉴 객체 생성 (식당 1의 메뉴)
		Menu menu1 = Menu.builder()
			.id(1L)
			.name("떡볶이")
			.description("맛있어요")
			.cuisineType(CuisineType.KOREAN)
			.restaurant(restaurant1)
			.price(10000)
			.build();

		// 임시 식당 객체 생성 (식당 2)
		Restaurant restaurant2 = Restaurant.builder()
			.id(2L)
			.minPrice(15000)
			.build();

		// 임시 메뉴 객체 생성 (식당 2의 메뉴)
		Menu menu2 = Menu.builder()
			.id(2L)
			.name("김밥")
			.description("맛있어요")
			.cuisineType(CuisineType.KOREAN)
			.restaurant(restaurant2)
			.price(8000)
			.build();

		// 주문 메뉴 DTO 생성 (식당 1의 메뉴와 식당 2의 메뉴 포함)
		OrderMenuDto orderMenuDto1 = OrderMenuDto.builder()
			.menuId(menu1.getId())
			.quantity(1L)
			.build();

		OrderMenuDto orderMenuDto2 = OrderMenuDto.builder()
			.menuId(menu2.getId())
			.quantity(1L)
			.build();

		// Mocking
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		when(menuRepository.findAllById(anyList())).thenReturn(List.of(menu1));
		when(menuRepository.findAllById(anyList())).thenReturn(List.of(menu2));

		// when & then
		assertThrows(IllegalArgumentException.class, () -> {
			orderService.createOrder(new VerifiedMember(memberId), List.of(orderMenuDto1, orderMenuDto2));
		});
	}
}
