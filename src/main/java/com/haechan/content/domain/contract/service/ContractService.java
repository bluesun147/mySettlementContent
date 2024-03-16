package com.haechan.content.domain.contract.service;

import com.haechan.content.domain.contract.dto.ContractExcelDto;
import com.haechan.content.domain.contract.entity.Contract;
import com.haechan.content.domain.contract.repository.ContractRepository;
import com.haechan.content.domain.contract.feign.DistributorFeignClient;
import com.haechan.content.domain.ost.entity.Ost;
import com.haechan.content.domain.ost.repository.OstRepository;
import com.haechan.content.global.config.ExcelHelper;
import com.haechan.feign.dto.ContractFeignResponse;
import com.haechan.feign.dto.DistributorFeignResponse;
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

import static com.haechan.content.global.BaseResponseStatus.INVALID_CONTRACT;
import static com.haechan.content.global.BaseResponseStatus.NON_EXIST_OST;


@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final OstRepository ostRepository;
    private final DistributorFeignClient distributorFeignClient;

    public void uploadFile(MultipartFile file) {
        try {
            List<ContractExcelDto> contractExcelDtoList = ExcelHelper.excelToContractList(file.getInputStream());
            List<Contract> contractList = new ArrayList<>();

            for (ContractExcelDto dto : contractExcelDtoList) {
                Ost ost = ostRepository.findById(dto.getOstId()).orElseThrow(() -> new IOException(NON_EXIST_OST.getMessage()));
                // Distributor distributor = distributorRepository.findById(dto.getDistributorId()).orElseThrow();

                Long memberId = dto.getDistributorId();
                log.info("memberId (dto dist id) = {}", memberId);
                DistributorFeignResponse distributorFeignResponse = distributorFeignClient.findDistributorById(memberId);
                Long distributorId = distributorFeignResponse.getDistributorId();
                log.info("distributorFeignResponse.getDistributorId() = {}", distributorId);
                log.info("distributorFeignResponse.getName() = {}", distributorFeignResponse.getName());



                Contract contract = Contract.builder()
                        .ost(ost)
                        .distributorId(distributorId)
                        .producerPercent(dto.getProducerPercent())
                        .singerPercent(dto.getSingerPercent())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .build();

                // 이미 있는 계약서인지 확인
                Optional<Contract> savedContract = contractRepository.findByOstAndDistributorId(ost, distributorId);
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

    public ContractFeignResponse findContractById(Long ostMemberId, Long distributorMemberId) {

        Ost ost = ostRepository.findById(ostMemberId).orElseThrow();
        log.info("ost.getTitle() = {}", ost.getTitle());


        Contract contract = contractRepository.findByOstAndDistributorId(ost, distributorMemberId).orElseThrow();
        log.info("contract.getId() = {}", contract.getId());

        // return new ContractFeignResponse(contract.getId(), contract.getProducerPercent(), contract.getSingerPercent());

        return ContractFeignResponse.builder()
                .contractId(contract.getId())
                .distributorId(contract.getDistributorId())
                .producerPercent(contract.getProducerPercent())
                .singerPercent(contract.getSingerPercent())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .build();

    }

    public ContractFeignResponse findContractById(Long memberId) {

        Contract contract = contractRepository.findById(memberId).orElseThrow();
        log.info("contract.getId() = {}", contract.getId());

        // return new ContractFeignResponse(contract.getId(), contract.getProducerPercent(), contract.getSingerPercent());
        return ContractFeignResponse.builder()
                .contractId(contract.getId())
                .ostId(contract.getOst().getId())
                .distributorId(contract.getDistributorId())
                .producerPercent(contract.getProducerPercent())
                .singerPercent(contract.getSingerPercent())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .build();
    }
}