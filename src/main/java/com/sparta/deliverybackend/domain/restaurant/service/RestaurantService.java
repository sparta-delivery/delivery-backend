package com.sparta.deliverybackend.domain.restaurant.service;

import com.sparta.deliverybackend.api.config.s3.S3Service;
import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.*;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;
    private final S3Service s3Service;

    public RestaurantCreateRepDto createRestaurant(RestaurantCreateReqDto reqDto, VerifiedMember verifiedMember, MultipartFile profileImg) {//로그인 유저로 바꿀 예정
        String url = s3Service.uploadImage(profileImg);

        Manager manager = managerRepository.findById(verifiedMember.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사장님 아이디입니다."));

        Restaurant restaurant = new Restaurant(reqDto.getName(), reqDto.getOpenTime(), reqDto.getCloseTime(), reqDto.getMinPrice(), manager);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return new RestaurantCreateRepDto(savedRestaurant);
    }

    public Page<RestaurantCreateRepDto> getRestaurants(Pageable pageable, VerifiedMember verifiedMember) {
        return restaurantRepository.findAll(pageable)
                .map(RestaurantCreateRepDto::new);
    }

    @Transactional
    public RestaurantUpdateRepDto updateRestaurant(Long restaurantId, RestaurantUpdateReqDto reqDto, VerifiedMember verifiedMember) {
        Manager manager = managerRepository.findById(verifiedMember.id())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사장님 아이디입니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new IllegalArgumentException("선택한 가게가 존재하지 않습니다."));

        restaurant.modify(reqDto.getName(), reqDto.getOpenTime(), reqDto.getCloseTime(), reqDto.getMinPrice(), manager);
        return new RestaurantUpdateRepDto(restaurant);
    }

    @Transactional
    public RestaurantDeleteRepDto deleteRestaurant(Long restaurantId, VerifiedMember verifiedMember) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 가게가 존재하지 않습니다."));

        restaurant.delete();

        restaurantRepository.save(restaurant);
        return new RestaurantDeleteRepDto(restaurant);
    }
}
