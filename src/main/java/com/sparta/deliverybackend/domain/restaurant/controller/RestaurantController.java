package com.sparta.deliverybackend.domain.restaurant.controller;

import com.sparta.deliverybackend.api.config.s3.S3Service;
import com.sparta.deliverybackend.api.controller.dto.VerifiedMember;
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
    private final S3Service s3Service;

//    @PostMapping("/restaurant")
//    public ResponseEntity<RestaurantCreateRepDto> createRestaurant(@RequestBody RestaurantCreateReqDto reqDto, VerifiedMember verifiedMember){//로그인 유저로 바꿀 예정
//        RestaurantCreateRepDto response = restaurantService.createRestaurant(reqDto, verifiedMember);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(response);
//    }

    //가게 이미지 업로드 기능 추가
    @RequestMapping(method = RequestMethod.POST, value = "/restaurant",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantCreateRepDto> createRestaurant(
            @RequestPart(value = "reqDto") RestaurantCreateReqDto reqDto,
            VerifiedMember verifiedMember,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg){//로그인 유저로 바꿀 예정
        RestaurantCreateRepDto response = restaurantService.createRestaurant(reqDto, verifiedMember, profileImg);
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

    @DeleteMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantDeleteRepDto> deleteRestaurant(@PathVariable Long restaurantId, VerifiedMember verifiedMember){
        RestaurantDeleteRepDto response = restaurantService.deleteRestaurant(restaurantId, verifiedMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    //s3에 이미지 올리는 예시용 참고 컨트롤러
    @RequestMapping(method = RequestMethod.POST, value = "/image",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageRespDto> createImage(@RequestPart(value = "title") String title, @RequestPart(value = "profileImg", required = false)MultipartFile profileImg){
       String url = s3Service.uploadImage(profileImg);
       ImageRespDto respDto = new ImageRespDto(title);
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(respDto);
    }




}
