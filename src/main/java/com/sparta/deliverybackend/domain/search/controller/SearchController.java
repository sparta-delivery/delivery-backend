package com.sparta.deliverybackend.domain.search.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.domain.search.controller.dto.SearchMenuRespDto;
import com.sparta.deliverybackend.domain.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

	private final SearchService searchService;

	@GetMapping("/search")
	public ResponseEntity<PagedModel<SearchMenuRespDto>> searchMenus(
		@RequestParam(required = false, defaultValue = "0") int page,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(value = "keyword", required = false) String keyword,
		Pageable pageable) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(new PagedModel<>(searchService.searchMenus(keyword, pageable)));
	}

}
