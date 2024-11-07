package com.sparta.deliverybackend.domain.search;

import org.springframework.data.jpa.domain.Specification;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;

public class MenuSpecification {
	
	public static Specification<Menu> likeMenuName(final String keyword) {
		return (root, query, cb) -> {
			assert query != null;
			query.distinct(true);
			return cb.like(root.get("name"), "%" + keyword + "%");
		};
	}

}
