package com.haechan.mysettlement.domain.settlement.repository;

import java.time.LocalDate;

public interface SettlementRepositoryCustom {
    Double findByTypeAndMemberIdAndSettleDate(Long type, Long memberId, LocalDate date);
}