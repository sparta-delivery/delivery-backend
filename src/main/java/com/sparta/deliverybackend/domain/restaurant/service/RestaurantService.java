package com.sparta.deliverybackend.domain.restaurant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.api.s3.service.S3Service;
import com.sparta.deliverybackend.domain.member.entity.Manager;
import com.sparta.deliverybackend.domain.member.repository.ManagerRepository;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.MenuRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantCreateRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantDeleteRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantUpdateReqDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantUpdateRespDto;
import com.sparta.deliverybackend.domain.restaurant.controller.dto.RestaurantViewRespDto;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.MenuRepository;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import com.sparta.deliverybackend.exception.customException.EtcException;
import com.sparta.deliverybackend.exception.customException.NotFoundEntityException;
import com.sparta.deliverybackend.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;
	private final ManagerRepository managerRepository;
	private final MenuRepository menuRepository;
	private final S3Service s3Service;

	public RestaurantCreateRespDto createRestaurant(RestaurantCreateReqDto reqDto, VerifiedMember verifiedMember,
		MultipartFile profileImg) {
		String url = s3Service.uploadImage(profileImg);

		// 해당 사용자가 소유한 가게의 수를 조회
		Long managerId = verifiedMember.id();
		long restaurantCount = restaurantRepository.countByManagerIdAndDeletedAtIsNull(managerId);

		// 사용자가 이미 3개의 가게를 추가했는지 확인
		if (restaurantCount >= 3) {
			throw new EtcException(ExceptionCode.TOO_MUCH_RESTAURANT);
		}

		Manager manager = managerRepository.findById(managerId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MANAGER));

		Restaurant restaurant = Restaurant.builder()
			.name(reqDto.getName())
			.openTime((reqDto.getOpenTime()))
			.closeTime(reqDto.getCloseTime())
			.minPrice(reqDto.getMinPrice())
			.manager((manager))
			.build();
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
		return new RestaurantCreateRespDto(savedRestaurant);
	}

	public Page<RestaurantCreateRespDto> getRestaurants(Pageable pageable, VerifiedMember verifiedMember) {
		return restaurantRepository.findAllByDeletedAtIsNull(pageable)
			.map(RestaurantCreateRespDto::new);
	}

	public RestaurantViewRespDto getRestaurantInfo(Long restaurantId, VerifiedMember verifiedMember) {
		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MANAGER));

		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_RESTAURANT));

		// Menu 리스트를 MenuRespDto 리스트로 변환
		List<MenuRespDto> menus = menuRepository.findByRestaurantId(restaurantId).stream()
			.map(menu -> new MenuRespDto(
				menu.getId(),
				menu.getName(),
				menu.getPrice(),
				menu.getDescription(),
				menu.getCuisineType(),
				menu.getRestaurant().getId(),
				menu.getCreatedAt(),
				menu.getUpdatedAt()
			))
			.collect(Collectors.toList());

		return new RestaurantViewRespDto(restaurant, menus);
	}

	@Transactional
	public RestaurantUpdateRespDto updateRestaurant(Long restaurantId, RestaurantUpdateReqDto reqDto,
		VerifiedMember verifiedMember) {
		Manager manager = managerRepository.findById(verifiedMember.id())
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MANAGER));

		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_RESTAURANT));

		restaurant.update(reqDto.getName(), reqDto.getOpenTime(), reqDto.getCloseTime(), reqDto.getMinPrice(), manager);
		return new RestaurantUpdateRespDto(restaurant);
	}

	@Transactional
	public RestaurantDeleteRespDto deleteRestaurant(Long restaurantId, VerifiedMember verifiedMember) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_RESTAURANT));

		restaurant.delete();

		restaurantRepository.save(restaurant);
		return new RestaurantDeleteRespDto(restaurant);
	}

}
