package com.haechan.member.domain.producer.repository;

import com.haechan.member.domain.producer.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}
