package com.haechan.member.domain.producer.controller;

import com.haechan.member.domain.producer.dto.ProducerDto;
import com.haechan.member.domain.producer.feign.ProducerFeignResponse;
import com.haechan.member.domain.producer.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @PostMapping("/")
    public void register(@RequestBody ProducerDto producerDto) {
        producerService.register(producerDto);
    }

    // ost등 다른 서버에서 producer 정보 API 통해 가져와야 함
    // memberId와 producerId가 다를 수 있다고 가정 (지금은 같음)
    @GetMapping
    public ProducerFeignResponse findProducerById(@RequestParam("memberId") Long memberId) {
        return producerService.findProducerById(memberId);
    }
}
