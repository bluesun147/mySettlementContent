package com.haechan.settlement.producer.controller;

import com.haechan.settlement.producer.dto.ProducerDto;
import com.haechan.settlement.producer.service.ProducerService;
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
