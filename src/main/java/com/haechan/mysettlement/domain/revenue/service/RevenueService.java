package com.haechan.mysettlement.domain.revenue.service;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.contract.repository.ContractRepository;
import com.haechan.mysettlement.global.config.ExcelHelper;
import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.distributor.repository.DistributorRepository;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import com.haechan.mysettlement.domain.ost.repository.OstRepository;
import com.haechan.mysettlement.domain.revenue.dto.RevenueExcelDto;
import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.revenue.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;
    private final OstRepository ostRepository;
    private final DistributorRepository distributorRepository;
    private final ContractRepository contractRepository;

    // 이거에 대한 테스트 만들어야 함.
    public void uploadFile(MultipartFile file) {
        try {
            List<RevenueExcelDto> revenueExcelDtoList = ExcelHelper.excelToRevenueList(file.getInputStream());
            List<Revenue> revenueList = new ArrayList<>();

            for (RevenueExcelDto dto : revenueExcelDtoList) {
                Ost ost = ostRepository.findById(dto.getOstId()).orElseThrow();
                Distributor distributor = distributorRepository.findById(dto.getDistributorId()).orElseThrow();

                Contract contract = contractRepository.findByOstAndDistributor(ost, distributor).orElseThrow();

                Revenue revenue = Revenue.builder()
                        .contract(contract)
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