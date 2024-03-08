package com.haechan.mysettlement.domain.producer.service;

import com.haechan.mysettlement.domain.producer.dto.ProducerDto;
import com.haechan.mysettlement.domain.producer.dto.ProducerHtmlSelectDto;
import com.haechan.mysettlement.domain.producer.repository.ProducerRepository;
import com.haechan.mysettlement.domain.producer.entity.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ProducerHtmlSelectDto> getProducerList() {
        List<Producer> producerList = producerRepository.findAll();
        List<ProducerHtmlSelectDto> producerDtoList = producerList.stream()
                .map(distributor -> ProducerHtmlSelectDto.builder()
                        .id(distributor.getId())
                        .name(distributor.getName())
                        .build()
                )
                .collect(Collectors.toList());
        return producerDtoList;
    }
}
