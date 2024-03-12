package com.haechan.finance.domain.revenue.service;

import com.haechan.feign.dto.*;
import com.haechan.finance.domain.revenue.dto.RevenueExcelDto;
import com.haechan.finance.domain.revenue.feign.OstFeignClient;
import com.haechan.finance.domain.revenue.feign.DistributorFeignClient;
import com.haechan.finance.domain.revenue.feign.ContractFeignClient;
import com.haechan.finance.domain.revenue.entity.Revenue;
import com.haechan.finance.domain.revenue.repository.RevenueRepository;
import com.haechan.finance.global.config.ExcelHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueService {

    private final RevenueRepository revenueRepository;
    private final OstFeignClient ostFeignClient;
    private final DistributorFeignClient distributorFeignClient;
    private final ContractFeignClient contractFeignClient;

    // 이거에 대한 테스트 만들어야 함.
    public void uploadFile(MultipartFile file) {
        try {
            List<RevenueExcelDto> revenueExcelDtoList = ExcelHelper.excelToRevenueList(file.getInputStream());
            List<Revenue> revenueList = new ArrayList<>();

            for (RevenueExcelDto dto : revenueExcelDtoList) {
                // Ost ost = ostRepository.findById(dto.getOstId()).orElseThrow();
                log.info("dto ostid = {}", dto.getOstId());
                OstFeignResponse ostFeignResponse = ostFeignClient.findOstById(dto.getOstId());

                // Distributor distributor = distributorRepository.findById(dto.getDistributorId()).orElseThrow();
                DistributorFeignResponse distributorFeignResponse = distributorFeignClient.findDistributorById(dto.getDistributorId());

                // Contract contract = contractRepository.findByOstAndDistributor(ost, distributor).orElseThrow();
                log.info("ostid = {}", ostFeignResponse.getOstId());
                log.info("distId = {}", distributorFeignResponse.getDistributorId());
                ContractFeignResponse contractFeignResponse = contractFeignClient.findByOstIdAndDistributorId(ostFeignResponse.getOstId(), distributorFeignResponse.getDistributorId());
                Long contractId = contractFeignResponse.getContractId();

                Revenue revenue = Revenue.builder()
                        .contractId(contractId)
                        .fee(dto.getFee())
                        .date(dto.getDate())
                        .build();

                revenueList.add(revenue);
            }
            // db에 저장
            revenueRepository.saveAll(revenueList);

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public Page<Revenue> getRevenueList(Pageable pageable) {
        return revenueRepository.findAll(pageable);
    }
}