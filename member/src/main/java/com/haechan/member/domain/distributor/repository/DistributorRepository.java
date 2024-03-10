package com.haechan.member.domain.distributor.repository;

import com.haechan.member.domain.distributor.entity.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {
}
