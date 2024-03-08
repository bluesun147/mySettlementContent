package com.haechan.mysettlement.domain.settlement.dto;

import com.haechan.mysettlement.domain.contract.dto.ContractDto;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SettlementDto {

    private ContractDto contractDto;
    private MemberType type;
    private Long memberId;
    private LocalDateTime settleDate;
    private Double fee;

    @Builder
    public SettlementDto(ContractDto contractDto, MemberType type, Long memberId, LocalDateTime settleDate, Double fee) {
        this.contractDto = contractDto;
        this.type = type;
        this.memberId = memberId;
        this.settleDate = settleDate;
        this.fee = fee;
    }

    public static SettlementDto of (Settlement settlement) {

        ContractDto contractDto = ContractDto.of(settlement.getContract());

        return SettlementDto.builder()
                .contractDto(contractDto)
                .type(settlement.getType())
                .memberId(settlement.getMemberId())
                .settleDate(settlement.getSettleDate())
                .fee(settlement.getFee())
                .build();
    }
}
