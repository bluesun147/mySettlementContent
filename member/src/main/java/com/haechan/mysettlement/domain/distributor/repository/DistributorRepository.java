package com.haechan.mysettlement.domain.distributor.repository;

import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {
}
