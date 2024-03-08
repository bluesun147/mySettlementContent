package com.haechan.mysettlement.domain.settlement.repository;

import com.haechan.mysettlement.domain.settlement.dto.MemberType;

public interface SettlementRepositoryCustom {
    Double findByTypeAndMemberIdAndSettleDate(int year, int month, MemberType type, Long memberId);
}