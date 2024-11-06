package com.sparta.deliverybackend.domain.search.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.sparta.deliverybackend.domain.restaurant.entity.CuisineType;
import com.sparta.deliverybackend.domain.restaurant.entity.Menu;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.domain.search.controller.dto.SearchMenuRespDto;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private SearchService searchService;

	List<Menu> mockMenus;

	@BeforeEach
	void setUp() {
		Restaurant restaurant = Restaurant.builder()
			.id(1L)
			.name("Restaurant")
			.openTime("11:00")
			.closeTime("23:00")
			.build();

		mockMenus = IntStream.range(0, 10)
			.mapToObj(
				i -> Menu.builder()
					.id((long)i)
					.name("Menu " + i)
					.price(i * 1000)
					.restaurant(restaurant)
					.description(i + "번째 메뉴의 설명")
					.cuisineType(CuisineType.KOREAN)
					.build()
			).toList();
	}

	@DisplayName("검색 기능 테스트, 일치하는 것이 있는 경우")
	@Test
	void searchMenus_withKeyWord() {
		//given
		String keyword = "Menu 0";
		PageRequest pageable = PageRequest.of(0, 5);

		// mock 데이터 생성: 예시로 필터링된 메뉴들을 사용
		List<Menu> filteredMenus = mockMenus.stream()
			.filter(menu -> menu.getName().contains(keyword))
			.toList();

		Page<Menu> menuPage = new PageImpl<>(filteredMenus, pageable, filteredMenus.size());
		when(menuRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(menuPage);

		//when
		Page<SearchMenuRespDto> result = searchService.searchMenus(keyword, pageable);

		//then
		assertThat(result).isNotNull();
		assertThat(result.getTotalPages()).isEqualTo(1); // 페이지 개수
		assertThat(result.getSize()).isEqualTo(5); // 페이지 크기
		assertThat(result.getContent().size()).isEqualTo(1); // 검색 결과 개수
		assertThat(result.getTotalElements()).isEqualTo(filteredMenus.size());
		assertThat(result.getContent().get(0).getMenuName()).isEqualTo("Menu 0");

	}

	@DisplayName("검색 기능 테스트, 일치하는 것이 없는 경우")
	@Test
	void searchMenus_withMismatchKeyWord() {
		//given
		String keyword = "얼렁뚱땅";
		PageRequest pageable = PageRequest.of(0, 5);

		// mock 데이터 생성: 예시로 필터링된 메뉴들을 사용
		List<Menu> filteredMenus = mockMenus.stream()
			.filter(menu -> menu.getName().contains(keyword))
			.toList();

		Page<Menu> menuPage = new PageImpl<>(filteredMenus, pageable, filteredMenus.size());
		when(menuRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(menuPage);

		//when
		Page<SearchMenuRespDto> result = searchService.searchMenus(keyword, pageable);

		//then
		assertThat(result).isNotNull();
		assertThat(result.getTotalPages()).isEqualTo(0); // 페이지 개수
		assertThat(result.getSize()).isEqualTo(5); // 페이지 크기
		assertThat(result.getContent().size()).isEqualTo(0); // 검색 결과 개수
		assertThat(result.getTotalElements()).isEqualTo(filteredMenus.size());

	}

	@Test
	void searchMenus_withoutKeyword() {
		// given
		String keyword = null; // 키워드 없이 검색
		PageRequest pageable = PageRequest.of(1, 5);
		Page<Menu> menuPage = new PageImpl<>(mockMenus, pageable, mockMenus.size());

		when(menuRepository.findAll(any(Specification.class), any(PageRequest.class)))
			.thenReturn(menuPage);

		// when
		Page<SearchMenuRespDto> result = searchService.searchMenus(keyword, pageable);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalPages()).isEqualTo(2); // 페이지 개수
		assertThat(result.getSize()).isEqualTo(5); // 페이지 크기
		assertThat(result.getContent().size()).isEqualTo(10); // 검색 결과 개수
		assertThat(result.getContent().get(0).getMenuName()).isEqualTo("Menu 0");

	}

}