package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageReqDto {
    private String title;

    public ImageReqDto(String title){
        this.title = title;
    }
}
