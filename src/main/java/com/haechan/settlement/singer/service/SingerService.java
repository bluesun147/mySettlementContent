package com.haechan.settlement.singer.service;

import com.haechan.settlement.singer.dto.SingerDto;
import com.haechan.settlement.singer.entity.Singer;
import com.haechan.settlement.singer.repository.SingerRepository;
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
