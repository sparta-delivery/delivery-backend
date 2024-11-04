package com.sparta.deliverybackend.domain.restaurant.controller;

import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantUpdateRepDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantUpdateReqDto;
import com.sparta.deliverybackend.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurant")
    public ResponseEntity<RestaurantCreateRepDto> createRestaurant(@RequestBody RestaurantCreateReqDto reqDto, VerifiedMember verifiedMember){//로그인 유저로 바꿀 예정
        RestaurantCreateRepDto response = restaurantService.createRestaurant(reqDto, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<Page<RestaurantCreateRepDto>> getRestaurants(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            VerifiedMember verifiedMember) {

        Page<RestaurantCreateRepDto> response = restaurantService.getRestaurants(pageable, verifiedMember);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantUpdateRepDto> updateRestaurant(@PathVariable Long restaurantId, @RequestBody RestaurantUpdateReqDto reqDto, VerifiedMember verifiedMember){
        RestaurantUpdateRepDto response = restaurantService.updateRestaurant(restaurantId, reqDto, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

//    @DeleteMapping("/restaurant/{restaurantId}")
//    public ResponseEntity<>
}
