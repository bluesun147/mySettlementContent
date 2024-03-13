package com.haechan.finance.domain.settlement.repository;

import com.haechan.finance.domain.settlement.entity.Settlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long>, SettlementRepositoryCustom {
    Page<Settlement> findAll(Pageable pageable);
}
