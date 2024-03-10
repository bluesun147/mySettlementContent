package com.haechan.mysettlement.domain.producer.repository;

import com.haechan.mysettlement.domain.producer.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}
