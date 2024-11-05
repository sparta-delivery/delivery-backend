package com.sparta.deliverybackend.domain.order.interceptor;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliverybackend.domain.order.controller.dto.OrderRequestDto;
import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;
import com.sparta.deliverybackend.domain.order.repository.OrderRepository;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderStatusValidationInterceptor implements HandlerInterceptor {

	private final OrderRepository orderRepository;
	private final RestaurantRepository restaurantRepository;
	private final MenuRepository menuRepository;
	private final ObjectMapper objectMapper;

	@Override
	public boolean preHandle(HttpServletRequest req,
		HttpServletResponse res,
		Object handler) throws
		Exception {

		ReadableRequestWrapper wrappedRequest;

		if ("POST".equals(req.getMethod())) {
			// 요청이 ReadableRequestWrapper 인지 확인
			if (req instanceof ReadableRequestWrapper) {
				wrappedRequest = (ReadableRequestWrapper)req; // 이미 래핑된 요청
			} else {
				// ReadableRequestWrapper 로 래핑
				wrappedRequest = new ReadableRequestWrapper(req);
			}

			// 요청 본문을 OrderRequestDto 로 변환
			OrderRequestDto reqDto = objectMapper.readValue(wrappedRequest.getReader(), OrderRequestDto.class);

			// menus 에서 첫 번째 메뉴 정보를 가져옴
			if (reqDto.getMenus() != null && !reqDto.getMenus().isEmpty()) {
				Long menuId = reqDto.getMenus().get(0).getMenuId();
				// 메뉴와 레스토랑 정보를 가져옵니다.
				Menu menu = menuRepository.findById(menuId)
					.orElseThrow(() -> new IllegalArgumentException("Menu not found"));

				// menu_id를 통해 레스토랑 조회
				Restaurant restaurant = restaurantRepository.findById(menu.getRestaurant().getId())
					.orElseThrow(() -> new IllegalArgumentException("Restaurant not found for the menu"));

				LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
				LocalTime openTime = LocalTime.parse(restaurant.getOpenTime(), DateTimeFormatter.ofPattern("HH:mm"));
				LocalTime closeTime = LocalTime.parse(restaurant.getCloseTime(), DateTimeFormatter.ofPattern("HH:mm"));

				// 클로즈 시간이 0시인 경우, 다음 날로 간주
				if (closeTime.equals(LocalTime.MIDNIGHT)) {
					closeTime = LocalTime.MAX; // 23:59:59.999999999로 설정
				}

				System.out.println("현재 시간: " + now);
				System.out.println("오픈 시간: " + openTime);
				System.out.println("클로즈 시간: " + closeTime);

				// 현재 시간이 영업시간 내인지 확인합니다.
				if (now.isBefore(openTime) || now.isAfter(closeTime)) {
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					res.getWriter().write("Order can be purposed in open time");
					return false;
				}
			}
			req.setAttribute("wrappedRequest", wrappedRequest);

		}

		if ("DELETE".equalsIgnoreCase(req.getMethod())) {

			String requestURI = req.getRequestURI();
			String[] uriParts = requestURI.split("/");
			Long orderIdParam = Long.valueOf(uriParts[3]);

			Order order = orderRepository.findById(orderIdParam)
				.orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderIdParam));

			if (order.getOrderStatus() != OrderStatus.WAIT) {
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				res.getWriter().write("Order can only be canceled in WAIT status.");
				return false;
			}

		}

		return true;
	}

}

