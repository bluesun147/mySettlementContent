package com.haechan.mysettlement.domain.contract.service;

import com.haechan.mysettlement.domain.contract.dto.ContractExcelDto;
import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.contract.repository.ContractRepository;
import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.distributor.repository.DistributorRepository;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import com.haechan.mysettlement.domain.ost.repository.OstRepository;
import com.haechan.mysettlement.domain.producer.entity.Producer;
import com.haechan.mysettlement.domain.producer.repository.ProducerRepository;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import com.haechan.mysettlement.domain.singer.repository.SingerRepository;
import com.haechan.mysettlement.global.config.ExcelHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final OstRepository ostRepository;
    private final DistributorRepository distributorRepository;

    public void uploadFile(MultipartFile file) {
        try {
            List<ContractExcelDto> contractExcelDtoList = ExcelHelper.excelToContractList(file.getInputStream());
            List<Contract> contractList = new ArrayList<>();

            for (ContractExcelDto dto : contractExcelDtoList) {
                Ost ost = ostRepository.findById(dto.getOstId()).orElseThrow();
                Distributor distributor = distributorRepository.findById(dto.getDistributorId()).orElseThrow();

                Contract contract = Contract.builder()
                        .ost(ost)
                        .distributor(distributor)
                        .producerPercent(dto.getProducerPercent())
                        .singerPercent(dto.getSingerPercent())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .build();

                contractList.add(contract);
            }
            // db에 저장
            contractRepository.saveAll(contractList);

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}