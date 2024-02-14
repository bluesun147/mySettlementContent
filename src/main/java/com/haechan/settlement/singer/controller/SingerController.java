package com.haechan.settlement.singer.controller;

import com.haechan.settlement.singer.dto.SingerDto;
import com.haechan.settlement.singer.service.SingerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/singer")
@RequiredArgsConstructor
public class SingerController {

    private final SingerService singerService;

    @PostMapping("/")
    public void register(@RequestBody SingerDto singerDto) {
        singerService.register(singerDto);
    }
}
