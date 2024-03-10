package com.haechan.mysettlement.domain.singer.repository;

import com.haechan.mysettlement.domain.singer.entity.Singer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingerRepository extends JpaRepository<Singer, Long> {
}
