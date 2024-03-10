package com.haechan.mysettlement.domain.settlement.repository;

import com.haechan.mysettlement.domain.settlement.dto.MemberType;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SettlementRepositoryCustom {
    Double getSumByTypeAndMemberIdAndSettleDate(int year, int month, MemberType type, Long memberId);
    Page<Settlement> findByTypeAndMemberIdAndSettleDate(int year, int month, MemberType type, Long memberId, Pageable pageable);
}