package com.sparta.deliverybackend.domain.restaurant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuCreateRequestDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuResponseDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuUpdateRequestDto;
import com.sparta.deliverybackend.domain.restaurant.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuController {

	private final MenuService menuService;

	// 메뉴 생성
	@PostMapping()
	public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuCreateRequestDto menuCreateRequestDto){
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(menuService.createMenu(menuCreateRequestDto));
	}

	// 메뉴 수정
	@PatchMapping("/{restaurantId}/menus/{menuId}")
	public ResponseEntity<Void> updateMenu(@PathVariable Long restaurantId,	@PathVariable Long menuId,
		@RequestBody @Valid MenuUpdateRequestDto menuUpdateRequestDto){
		menuService.updateMenu(restaurantId, menuId, menuUpdateRequestDto);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	// 메뉴 삭제
	@DeleteMapping("/{restaurantId}/menus/{menuId}")
	public ResponseEntity<Void> deleteMenu(@PathVariable Long restaurantId,
		@PathVariable Long menuId){
		menuService.deleteMenu(restaurantId, menuId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}


}
