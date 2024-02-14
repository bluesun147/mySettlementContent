package com.haechan.settlement.singer.repository;

import com.haechan.settlement.singer.entity.Singer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingerRepository extends JpaRepository<Singer, Long> {
}
