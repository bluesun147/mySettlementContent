package com.haechan.mysettlement.domain.producer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProducerHtmlSelectDto {
    private Long id;
    private String name;

    @Builder
    ProducerHtmlSelectDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
