package com.haechan.mysettlement.domain.revenue.repository;

import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface RevenueRepositoryCustom {
    List<Revenue> findByDate(LocalDate date);
    Page<Revenue> findByThisMonth(LocalDate date, Pageable pageable);
}