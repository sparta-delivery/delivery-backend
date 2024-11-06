package com.sparta.deliverybackend.domain.member.controller;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantAddRespDto;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantDeleteRespDto;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantViewRespDto;
import com.sparta.deliverybackend.domain.member.service.FavoriteRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{restaurantId}/favorite")
    public ResponseEntity<FavoriteRestaurantDeleteRespDto> deleteFavoriteRestaurant(@PathVariable(name = "restaurantId") Long restaurantId, VerifiedMember verifiedMember){
        FavoriteRestaurantDeleteRespDto response = favoriteRestaurantService.deleteFavoriteRestaurant(restaurantId, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/favorite")
    public ResponseEntity<List<FavoriteRestaurantViewRespDto>> getFavoriteRestaurants(VerifiedMember verifiedMember){
        List<FavoriteRestaurantViewRespDto> response = favoriteRestaurantService.getFavoriteRestaurants(verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
