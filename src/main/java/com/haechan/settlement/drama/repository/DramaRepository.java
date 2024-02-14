package com.haechan.settlement.drama.repository;

import com.haechan.settlement.drama.entity.Drama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DramaRepository extends JpaRepository<Drama, Long> {
}
