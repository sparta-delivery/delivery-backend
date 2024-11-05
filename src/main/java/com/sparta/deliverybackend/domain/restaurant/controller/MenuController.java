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

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuUpdateReqDto;
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
	public ResponseEntity<MenuRespDto> createMenu(@RequestBody MenuCreateReqDto menuCreateReqDto,
		VerifiedMember verifiedMember) {
		MenuRespDto menuRespDto = menuService.createMenu(menuCreateReqDto, verifiedMember);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(menuRespDto);
	}

	// 메뉴 수정
	@PatchMapping("/{restaurantId}/menus/{menuId}")
	public ResponseEntity<Void> updateMenu(@PathVariable Long restaurantId, @PathVariable Long menuId,
		@RequestBody @Valid MenuUpdateReqDto menuUpdateReqDto, VerifiedMember verifiedMember) {
		menuService.updateMenu(restaurantId, menuId, menuUpdateReqDto, verifiedMember);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	// 메뉴 삭제
	@DeleteMapping("/{restaurantId}/menus/{menuId}")
	public ResponseEntity<Void> deleteMenu(@PathVariable Long restaurantId, @PathVariable Long menuId,
		VerifiedMember verifiedMember) {
		menuService.deleteMenu(restaurantId, menuId, verifiedMember);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}
}
