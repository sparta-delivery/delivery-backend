package com.sparta.deliverybackend.domain.restaurant.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageRespDto {
    private String title;

    public ImageRespDto(String title){
        this.title = title;
    }

}
