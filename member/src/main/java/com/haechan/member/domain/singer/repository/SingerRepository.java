package com.haechan.member.domain.singer.repository;

import com.haechan.member.domain.singer.entity.Singer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingerRepository extends JpaRepository<Singer, Long> {
}
