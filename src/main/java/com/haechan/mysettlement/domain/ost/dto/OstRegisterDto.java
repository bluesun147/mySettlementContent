package com.haechan.mysettlement.domain.ost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OstRegisterDto {
    private Long dramaId;
    private Long producerId;
    private Long singerId;
    private String title;
}
