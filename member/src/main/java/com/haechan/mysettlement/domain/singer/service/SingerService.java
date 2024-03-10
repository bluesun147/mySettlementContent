package com.haechan.mysettlement.domain.singer.service;

import com.haechan.mysettlement.domain.singer.dto.SingerDto;
import com.haechan.mysettlement.domain.singer.dto.SingerHtmlSelectDto;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import com.haechan.mysettlement.domain.singer.repository.SingerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<SingerHtmlSelectDto> getSingerList() {
        List<Singer> singerList = singerRepository.findAll();
        List<SingerHtmlSelectDto> singerDtoList = singerList.stream()
                .map(singer -> SingerHtmlSelectDto.builder()
                        .id(singer.getId())
                        .name(singer.getName())
                        .build()
                )
                .collect(Collectors.toList());
        return singerDtoList;
    }
}
