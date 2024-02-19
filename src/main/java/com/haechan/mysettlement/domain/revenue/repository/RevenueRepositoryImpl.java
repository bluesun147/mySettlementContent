package com.haechan.mysettlement.domain.revenue.repository;

import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.haechan.mysettlement.domain.revenue.entity.QRevenue.revenue;

@Repository
@RequiredArgsConstructor
public class RevenueRepositoryImpl implements RevenueRepositoryCustom {
    private final JPAQueryFactory query;
    @Override
    public List<Revenue> findByDate(LocalDate date) {
        return query.select(revenue)
                .from(revenue)
                .where(revenue.date.year().eq(date.getYear()))
                .where(revenue.date.month().eq(date.getMonthValue()))
                .fetch();
    }
}