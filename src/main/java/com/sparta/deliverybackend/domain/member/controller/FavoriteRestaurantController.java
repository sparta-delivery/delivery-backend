package com.sparta.deliverybackend.domain.member.controller;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantAddRespDto;
import com.sparta.deliverybackend.domain.member.service.FavoriteRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class FavoriteRestaurantController {
    private final FavoriteRestaurantService favoriteRestaurantService;

    @PostMapping("/{restaurantId}/favorite")
    public ResponseEntity<FavoriteRestaurantAddRespDto> addFavoriteRestaurant(@PathVariable(name = "restaurantId") Long restaurantId, VerifiedMember verifiedMember){
        FavoriteRestaurantAddRespDto response = favoriteRestaurantService.addFavoriteRestaurant(restaurantId, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
