package com.haechan.mysettlement.domain.singer.service;

import com.haechan.mysettlement.domain.singer.dto.SingerDto;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import com.haechan.mysettlement.domain.singer.repository.SingerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SingerService {

    private final SingerRepository singerRepository;

    public Long register(SingerDto singerDto) {

        Singer singer = Singer.builder()
                .name(singerDto.getName())
                .build();

        return singerRepository.save(singer).getId();
    }
}
