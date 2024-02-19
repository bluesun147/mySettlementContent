package com.haechan.mysettlement.domain.revenue.repository;

import com.haechan.mysettlement.domain.revenue.entity.Revenue;

import java.time.LocalDate;
import java.util.List;

public interface RevenueRepositoryCustom {
    List<Revenue> findByDate(LocalDate date);
}