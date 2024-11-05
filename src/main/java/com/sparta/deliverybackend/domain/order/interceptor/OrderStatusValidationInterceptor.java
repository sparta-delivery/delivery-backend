package com.sparta.deliverybackend.domain.order.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sparta.deliverybackend.domain.order.entity.Order;
import com.sparta.deliverybackend.domain.order.entity.OrderStatus;
import com.sparta.deliverybackend.domain.order.repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderStatusValidationInterceptor implements HandlerInterceptor {

	private final OrderRepository orderRepository;

	@Override
	public boolean preHandle(HttpServletRequest req,
		HttpServletResponse res,
		Object handler) throws
		Exception {

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
