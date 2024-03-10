package com.haechan.member.domain.producer.service;

import com.haechan.member.domain.producer.dto.ProducerDto;
import com.haechan.member.domain.producer.dto.ProducerHtmlSelectDto;
import com.haechan.member.domain.producer.feign.ProducerFeignResponse;
import com.haechan.member.domain.producer.repository.ProducerRepository;
import com.haechan.member.domain.producer.entity.Producer;
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

    // ost등 다른 서버에서 producer 정보 API 통해 가져와야 함
    public ProducerFeignResponse findProducerById(Long producerId) {
        Producer producer = producerRepository.findById(producerId).orElseThrow();
        return new ProducerFeignResponse(producer.getId(), producer.getName());
    }
}
