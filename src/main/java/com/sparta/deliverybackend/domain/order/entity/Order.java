package com.sparta.deliverybackend.domain.order.entity;

import java.time.LocalDateTime;

import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.exception.customException.EtcException;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Column
	@Enumerated(value = EnumType.STRING)
	private OrderStatus orderStatus;

	@Column
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime completedAt;

	public void updateOrderStatus() {
		this.orderStatus = this.orderStatus.next();
		if (this.orderStatus == OrderStatus.COMPLETE) {
			this.completeOrder();
		}
	}

	public void completeOrder() {
		this.orderStatus = OrderStatus.COMPLETE;
		if (this.completedAt == null) {
			this.completedAt = LocalDateTime.now();
		}
	}

	public void validateStatusIsComplete() {
		if (!orderStatus.equals(OrderStatus.COMPLETE)) {
			throw new EtcException(ExceptionCode.NO_COMPLETED_ORDER);
		}
	}

	public void validateOrderedMember(Member otherMember) {
		member.validateAuthority(otherMember);
	}

	public void cancelOrder() {
		orderStatus = OrderStatus.CANCELED;
	}
}
