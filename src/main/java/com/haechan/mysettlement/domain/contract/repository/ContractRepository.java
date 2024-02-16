package com.haechan.mysettlement.domain.contract.repository;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
