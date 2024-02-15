package com.haechan.mysettlement.domain.producer.service;

import com.haechan.mysettlement.domain.producer.dto.ProducerDto;
import com.haechan.mysettlement.domain.producer.repository.ProducerRepository;
import com.haechan.mysettlement.domain.producer.entity.Producer;
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
