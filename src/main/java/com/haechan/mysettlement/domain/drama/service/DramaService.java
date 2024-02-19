package com.haechan.mysettlement.domain.drama.service;

import com.haechan.mysettlement.domain.drama.dto.DramaDto;
import com.haechan.mysettlement.domain.drama.repository.DramaRepository;
import com.haechan.mysettlement.domain.drama.entity.Drama;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DramaService {

    private final DramaRepository dramaRepository;

    public void register(DramaDto dramaDto) {

        Drama drama = Drama.builder()
                .title(dramaDto.getTitle())
                .build();

        dramaRepository.save(drama);
    }
}
