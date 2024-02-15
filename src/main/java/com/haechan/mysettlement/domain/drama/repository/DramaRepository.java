package com.haechan.mysettlement.domain.drama.repository;

import com.haechan.mysettlement.domain.drama.entity.Drama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DramaRepository extends JpaRepository<Drama, Long> {
}
