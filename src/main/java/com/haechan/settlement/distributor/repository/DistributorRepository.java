package com.haechan.settlement.distributor.repository;

import com.haechan.settlement.distributor.entity.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {
}
