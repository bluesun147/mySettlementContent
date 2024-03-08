package com.haechan.mysettlement.domain.settlement.dto;

import lombok.Getter;

@Getter
public enum MemberType {

    DISTRIBUTOR(0),
    SINGER(1),
    PRODUCER(2),
    COMPANY(3);

    private final int type;

    MemberType(int type) {
        this.type = type;
    }
}