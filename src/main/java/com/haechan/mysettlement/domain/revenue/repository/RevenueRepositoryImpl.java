package com.haechan.mysettlement.domain.revenue.repository;

import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

    // 이번달 revenue 찾기
    @Override
    public Page<Revenue> findByThisMonth(LocalDate date, Pageable pageable) {
        List<Revenue> fetch = query.select(revenue)
                .from(revenue)
                .where(revenue.date.year().eq(date.getYear()))
                .where(revenue.date.month().eq(date.getMonthValue()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // return new PageImpl(fetch);
        // https://junior-datalist.tistory.com/342

        JPAQuery<Long> count = query.select(revenue.count())
                .from(revenue)
                .where(revenue.date.year().eq(date.getYear()))
                .where(revenue.date.month().eq(date.getMonthValue()));

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }
}