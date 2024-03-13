package com.haechan.content.domain.drama.controller;

import com.haechan.content.domain.drama.dto.DramaDto;
import com.haechan.content.domain.drama.service.DramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/drama")
@RequiredArgsConstructor
public class DramaController {

    private final DramaService dramaService;

    @PostMapping("/")
    public void register(@RequestBody DramaDto dramaDto) {
        dramaService.register(dramaDto);
    }
}
