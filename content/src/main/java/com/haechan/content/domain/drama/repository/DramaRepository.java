package com.haechan.content.domain.drama.repository;

import com.haechan.content.domain.drama.entity.Drama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DramaRepository extends JpaRepository<Drama, Long> {
}
