package com.haechan.settlement.drama.service;

import com.haechan.settlement.drama.dto.DramaDto;
import com.haechan.settlement.drama.entity.Drama;
import com.haechan.settlement.drama.repository.DramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DramaService {

    private final DramaRepository dramaRepository;

    public void register(DramaDto dramaDto) {

        Drama drama = Drama.builder()
                .name(dramaDto.getTitle())
                .build();

        dramaRepository.save(drama);
    }
}
