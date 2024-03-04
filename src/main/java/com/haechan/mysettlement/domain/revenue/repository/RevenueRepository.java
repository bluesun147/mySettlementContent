package com.haechan.mysettlement.domain.revenue.repository;

import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RevenueRepository extends JpaRepository<Revenue, Long>, RevenueRepositoryCustom {
    Page<Revenue> findByDateAfter(LocalDateTime date, Pageable pageable);

}