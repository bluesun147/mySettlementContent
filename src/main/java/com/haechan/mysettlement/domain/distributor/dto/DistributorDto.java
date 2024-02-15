package com.haechan.mysettlement.domain.distributor.dto;

import lombok.Getter;

@Getter
public class DistributorDto {
    private String name;

    // 유통사 분배 비율
    private Double percent;

}
