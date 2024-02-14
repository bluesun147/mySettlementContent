package com.haechan.settlement.distributor.service;

import com.haechan.settlement.distributor.dto.DistributorDto;
import com.haechan.settlement.distributor.entity.Distributor;
import com.haechan.settlement.distributor.repository.DistributorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
