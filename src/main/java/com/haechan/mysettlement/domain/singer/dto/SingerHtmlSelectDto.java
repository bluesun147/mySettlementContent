package com.haechan.mysettlement.domain.singer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingerHtmlSelectDto {
    private Long id;
    private String name;

    @Builder
    SingerHtmlSelectDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
