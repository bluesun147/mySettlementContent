package com.haechan.mysettlement.domain.settlement.repository;

import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.haechan.mysettlement.domain.settlement.entity.QSettlement.settlement;

@Repository
@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepositoryCustom {
    private final JPAQueryFactory query;
    @Override
    public Double findByTypeAndMemberIdAndSettleDate(Long type, Long memberId, LocalDate date) {
        return query.select(settlement.fee.sum())
                .from(settlement)
                .where(settlement.type.eq(type))
                .where(settlement.memberId.eq(memberId))
                .where(settlement.settleDate.year().eq(date.getYear()))
                .where(settlement.settleDate.month().eq(date.getMonthValue()))
                .fetchOne();
    }
}