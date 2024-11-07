package com.sparta.deliverybackend.domain.restaurant.controller;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.*;
import com.sparta.deliverybackend.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantService restaurantService;

    //가게 이미지 업로드 기능 추가
    @RequestMapping(method = RequestMethod.POST, value = "/restaurant",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantCreateRespDto> createRestaurant(
            @RequestPart(value = "reqDto") RestaurantCreateReqDto reqDto,
            VerifiedMember verifiedMember,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg){//로그인 유저로 바꿀 예정
        RestaurantCreateRespDto response = restaurantService.createRestaurant(reqDto, verifiedMember, profileImg);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<Page<RestaurantCreateRespDto>> getRestaurants(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            VerifiedMember verifiedMember) {

        Page<RestaurantCreateRespDto> response = restaurantService.getRestaurants(pageable, verifiedMember);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantViewRespDto> getRestaurantInfo(@PathVariable Long restaurantId, VerifiedMember verifiedMember){
        RestaurantViewRespDto response = restaurantService.getRestaurantInfo(restaurantId, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantUpdateRespDto> updateRestaurant(@PathVariable Long restaurantId, @RequestBody RestaurantUpdateReqDto reqDto, VerifiedMember verifiedMember){
        RestaurantUpdateRespDto response = restaurantService.updateRestaurant(restaurantId, reqDto, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantDeleteRespDto> deleteRestaurant(@PathVariable Long restaurantId, VerifiedMember verifiedMember){
        RestaurantDeleteRespDto response = restaurantService.deleteRestaurant(restaurantId, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
