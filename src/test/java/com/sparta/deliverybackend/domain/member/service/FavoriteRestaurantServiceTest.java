package com.sparta.deliverybackend.domain.member.service;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantAddRespDto;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantDeleteRespDto;
import com.sparta.deliverybackend.domain.member.controller.dto.FavoriteRestaurantViewRespDto;
import com.sparta.deliverybackend.domain.member.entity.FavoriteRestaurant;
import com.sparta.deliverybackend.domain.member.entity.Member;
import com.sparta.deliverybackend.domain.member.repository.FavoriteRestaurantRepository;
import com.sparta.deliverybackend.domain.member.repository.MemberRepository;
import com.sparta.deliverybackend.domain.restaurant.entity.Restaurant;
import com.sparta.deliverybackend.domain.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FavoriteRestaurantServiceTest {

    @InjectMocks
    private FavoriteRestaurantService favoriteRestaurantService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private FavoriteRestaurantRepository favoriteRestaurantRepository;

    private VerifiedMember verifiedMember;
    private Member member;
    private Restaurant restaurant;
    private FavoriteRestaurant favoriteRestaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트용 데이터 설정
        verifiedMember = new VerifiedMember(1L);
        member = new Member();
        setIdUsingReflection(member, 1L); // Reflection으로 id 설정

        restaurant = new Restaurant();
        restaurant.setId(1L);

        favoriteRestaurant = new FavoriteRestaurant(restaurant, member);
    }

    @Test
    @DisplayName("즐겨찾기 생성 테스트")
    void addFavoriteRestaurant_NewFavorite() {
        // Given
        when(memberRepository.findById(verifiedMember.id())).thenReturn(Optional.of(member));
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(favoriteRestaurantRepository.findByRestaurantAndMember(restaurant, member)).thenReturn(Optional.empty());

        // 새로운 즐겨찾기 객체 생성 (기존 즐겨찾기가 없을 때)
        FavoriteRestaurant newFavorite = new FavoriteRestaurant(restaurant, member);

        setIdUsingReflection(newFavorite, 1L);  // newFavorite 객체에 id 설정

        when(favoriteRestaurantRepository.save(Mockito.any(FavoriteRestaurant.class))).thenReturn(newFavorite);
        FavoriteRestaurantAddRespDto response = favoriteRestaurantService.addFavoriteRestaurant(restaurant.getId(), verifiedMember);

        // Then: response가 null이 아니고, 반환된 restaurantId가 일치하는지 확인
        assertNotNull(response);
        assertEquals(restaurant.getId(), response.getRestaurantId());

        verify(favoriteRestaurantRepository).save(Mockito.any(FavoriteRestaurant.class)); // save() 호출 검증
    }


    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void deleteFavoriteRestaurant() {
        // Given
        when(memberRepository.findById(verifiedMember.id())).thenReturn(Optional.of(member));
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(favoriteRestaurantRepository.findByRestaurantAndMember(restaurant, member)).thenReturn(Optional.of(favoriteRestaurant));

        // When
        FavoriteRestaurantDeleteRespDto response = favoriteRestaurantService.deleteFavoriteRestaurant(restaurant.getId(), verifiedMember);

        // Then
        assertNotNull(response);
        assertNotNull(favoriteRestaurant.getDeletedAt());
        verify(favoriteRestaurantRepository).save(favoriteRestaurant);
    }

    @Test
    @DisplayName("즐겨찾기 조회 테스트")
    void getFavoriteRestaurants() {
        // Given
        when(memberRepository.findById(verifiedMember.id())).thenReturn(Optional.of(member));
        when(favoriteRestaurantRepository.findAllByMemberAndDeletedAtIsNull(member)).thenReturn(Collections.singletonList(favoriteRestaurant));

        // When
        List<FavoriteRestaurantViewRespDto> response = favoriteRestaurantService.getFavoriteRestaurants(verifiedMember);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(favoriteRestaurantRepository).findAllByMemberAndDeletedAtIsNull(member);
    }

    private void setIdUsingReflection(Object target, Long idValue) {
        try {
            var idField = target.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(target, idValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set id via reflection", e);
        }
    }
}