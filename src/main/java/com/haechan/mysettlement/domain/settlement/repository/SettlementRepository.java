package com.haechan.mysettlement.domain.settlement.repository;

import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SettlementRepository extends JpaRepository<Settlement, Long>, SettlementRepositoryCustom {
}
