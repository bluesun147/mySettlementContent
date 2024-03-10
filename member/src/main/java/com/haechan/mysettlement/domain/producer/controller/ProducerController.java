package com.haechan.mysettlement.domain.producer.controller;

import com.haechan.mysettlement.domain.producer.dto.ProducerDto;
import com.haechan.mysettlement.domain.producer.service.ProducerService;
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
}
