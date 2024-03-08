package com.haechan.mysettlement.domain.distributor.service;

import com.haechan.mysettlement.domain.distributor.dto.DistributorDto;
import com.haechan.mysettlement.domain.distributor.dto.DistributorHtmlSelectDto;
import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.distributor.repository.DistributorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistributorService {

    private final DistributorRepository distributorRepository;

    public void register(DistributorDto distributorDto) {

        Distributor distributor = Distributor.builder()
                .name(distributorDto.getName())
                .percent(distributorDto.getPercent())
                .build();

        distributorRepository.save(distributor);
    }

    public List<DistributorHtmlSelectDto> getDistributorList() {
        List<Distributor> distributorList = distributorRepository.findAll();
        List<DistributorHtmlSelectDto> distributorDtoList = distributorList.stream()
                .map(distributor -> DistributorHtmlSelectDto.builder()
                        .id(distributor.getId())
                        .name(distributor.getName())
                        .build()
                )
                .collect(Collectors.toList());
        return distributorDtoList;
    }
}
