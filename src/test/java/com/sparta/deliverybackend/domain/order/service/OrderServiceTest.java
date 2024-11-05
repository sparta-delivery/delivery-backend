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

		// 임시 멤버 객체 생성
		Long memberId = 1L;
		Member member = Member.builder()
			.id(memberId)
			.nickname("태우")
			.email("wootaepark@naver.com")
			.password("1234")
			.joinPath(JoinPath.OAUTH)
			.build();

		// 임시 식당 객체 생성
		Restaurant restaurant = Restaurant.builder().id(1L).build();

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
			.menuId(1L)
			.quantity(2L)
			.build();

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		when(menuRepository.findById(orderMenuDto.getMenuId())).thenReturn(Optional.of(menu));

		Order order = Order.builder()
			.id(1L)
			.orderStatus(OrderStatus.WAIT)
			.member(member)
			.createdAt(LocalDateTime.now())
			.build();

		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(orderMenuRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		OrderResponseDto orderResponseDto = orderService.createOrder(new VerifiedMember(memberId),
			List.of(orderMenuDto));

		// then
		assertNotNull(orderResponseDto);
		assertEquals("태우", orderResponseDto.getCustomer());
		assertEquals(1L, orderResponseDto.getRequestMenus().get(0).getMenuId());
		assertEquals(2L, orderResponseDto.getRequestMenus().get(0).getQuantity());
		assertEquals(OrderStatus.WAIT, orderResponseDto.getOrderStatus());
	}
}