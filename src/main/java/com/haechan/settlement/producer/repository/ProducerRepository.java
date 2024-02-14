package com.haechan.settlement.producer.repository;

import com.haechan.settlement.producer.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}
