package com.sparta.deliverybackend.domain.member.controller.dto;

import com.sparta.deliverybackend.api.auth.controller.dto.VerifiedMember;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRestaurantAddReqDto {
    private VerifiedMember memberId;
}
