package com.haechan.mysettlement.domain.settlement.repository;

import com.haechan.mysettlement.domain.settlement.dto.MemberType;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.haechan.mysettlement.domain.settlement.entity.QSettlement.settlement;

@Repository
@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepositoryCustom {
    private final JPAQueryFactory query;
    @Override
    public Double getSumByTypeAndMemberIdAndSettleDate(int year, int month, MemberType type, Long memberId) {

        return query.select(settlement.fee.sum())
                .from(settlement)
                .where(settlement.settleDate.year().eq(year))
                .where(settlement.settleDate.month().eq(month))
                .where(settlement.type.eq(type))
                .where(settlement.memberId.eq(memberId))
                .fetchOne();

    }

    @Override
    public Page<Settlement> findByTypeAndMemberIdAndSettleDate(int year, int month, MemberType type, Long memberId, Pageable pageable) {

        List<Settlement> fetch = query.select(settlement)
                .from(settlement)
                .where(settlement.settleDate.year().eq(year))
                .where(settlement.settleDate.month().eq(month))
                .where(settlement.type.eq(type))
                .where(settlement.memberId.eq(memberId))
                .fetch();

        JPAQuery<Long> count = query.select(settlement.count())
                .from(settlement)
                .where(settlement.settleDate.year().eq(year))
                .where(settlement.settleDate.month().eq(month))
                .where(settlement.type.eq(type))
                .where(settlement.memberId.eq(memberId));

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }
}