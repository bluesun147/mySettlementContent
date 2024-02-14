package com.haechan.settlement.producer.service;

import com.haechan.settlement.producer.dto.ProducerDto;
import com.haechan.settlement.producer.entity.Producer;
import com.haechan.settlement.producer.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;

    public void register(ProducerDto producerDto) {

        Producer producer = Producer.builder()
                .name(producerDto.getName())
                .build();

        producerRepository.save(producer);
    }
}
