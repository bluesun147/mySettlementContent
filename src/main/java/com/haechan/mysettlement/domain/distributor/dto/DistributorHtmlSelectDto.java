package com.haechan.mysettlement.domain.distributor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DistributorHtmlSelectDto {
    private Long id;
    private String name;

    @Builder
    DistributorHtmlSelectDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
