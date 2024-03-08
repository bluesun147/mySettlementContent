package com.haechan.mysettlement.domain.contract.service;

import com.haechan.mysettlement.domain.contract.dto.ContractExcelDto;
import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.contract.repository.ContractRepository;
import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.distributor.repository.DistributorRepository;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import com.haechan.mysettlement.domain.ost.repository.OstRepository;
import com.haechan.mysettlement.global.BaseException;
import com.haechan.mysettlement.global.BaseResponseStatus;
import com.haechan.mysettlement.global.config.ExcelHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.haechan.mysettlement.global.BaseResponseStatus.INVALID_CONTRACT;


@Slf4j
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

                // 이미 있는 계약서인지 확인
                Optional<Contract> savedContract = contractRepository.findByOstAndDistributor(ost, distributor);
                System.out.println("savedContract = " + savedContract);

                // 이미 db에 있는 계약서 일 때
                if (savedContract.isPresent()) {
                    throw new IOException(INVALID_CONTRACT.getMessage() + " id = " + savedContract.get().getId());
                }

                contractList.add(contract);
            }
            // db에 저장
            contractRepository.saveAll(contractList);
            log.info("save success!");

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public Page<Contract> getContractList(Pageable pageable) {
        return contractRepository.findAll(pageable);
    }
}