package com.haechan.mysettlement.domain.settlement.repository;

import com.haechan.mysettlement.domain.settlement.dto.MemberType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.haechan.mysettlement.domain.settlement.entity.QSettlement.settlement;

@Repository
@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepositoryCustom {
    private final JPAQueryFactory query;
    @Override
    public Double findByTypeAndMemberIdAndSettleDate(int year, int month, MemberType type, Long memberId) {

        return query.select(settlement.fee.sum())
                .from(settlement)
                .where(settlement.settleDate.year().eq(year))
                .where(settlement.settleDate.month().eq(month))
                .where(settlement.type.eq(type))
                .where(settlement.memberId.eq(memberId))
                .fetchOne();

    }
}