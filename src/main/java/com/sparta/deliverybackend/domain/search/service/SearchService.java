package com.sparta.deliverybackend.domain.search.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.domain.search.MenuSpecification;
import com.sparta.deliverybackend.domain.search.controller.dto.SearchMenuRespDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final MenuRepository menuRepository;

	public Page<SearchMenuRespDto> searchMenus(String keyword, Pageable pageable) {
		Specification<Menu> spec = Specification.where(null);
		if (keyword != null) {
			spec = spec.and(MenuSpecification.likeMenuName(keyword));
		}

		Page<Menu> menuPage = menuRepository.findAll(spec, pageable);

		List<SearchMenuRespDto> searchMenuRespDtos = SearchMenuRespDto.from(menuPage);

		return new PageImpl<>(searchMenuRespDtos, pageable, menuPage.getTotalElements());
	}

}


