//package com.haechan.finance.domain.revenue.service;
//
//import com.haechan.finance.domain.contract.entity.Contract;
//import com.haechan.finance.domain.contract.repository.ContractRepository;
//import com.haechan.finance.global.config.ExcelHelper;
//import com.haechan.finance.domain.distributor.entity.Distributor;
//import com.haechan.finance.domain.distributor.repository.DistributorRepository;
//import com.haechan.finance.domain.ost.entity.Ost;
//import com.haechan.finance.domain.ost.repository.OstRepository;
//import com.haechan.finance.domain.revenue.dto.RevenueExcelDto;
//import com.haechan.finance.domain.revenue.entity.Revenue;
//import com.haechan.finance.domain.revenue.repository.RevenueRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RevenueService {
//
//    private final RevenueRepository revenueRepository;
//    private final OstRepository ostRepository;
//    private final DistributorRepository distributorRepository;
//    private final ContractRepository contractRepository;
//
//    // 이거에 대한 테스트 만들어야 함.
//    public void uploadFile(MultipartFile file) {
//        try {
//            List<RevenueExcelDto> revenueExcelDtoList = ExcelHelper.excelToRevenueList(file.getInputStream());
//            List<Revenue> revenueList = new ArrayList<>();
//
//            for (RevenueExcelDto dto : revenueExcelDtoList) {
//                Ost ost = ostRepository.findById(dto.getOstId()).orElseThrow();
//                Distributor distributor = distributorRepository.findById(dto.getDistributorId()).orElseThrow();
//
//                Contract contract = contractRepository.findByOstAndDistributor(ost, distributor).orElseThrow();
//
//                Revenue revenue = Revenue.builder()
//                        .contract(contract)
//                        .fee(dto.getFee())
//                        .date(dto.getDate())
//                        .build();
//
//                revenueList.add(revenue);
//            }
//            // db에 저장
//            revenueRepository.saveAll(revenueList);
//
//        } catch (IOException e) {
//            throw new RuntimeException("fail to store excel data: " + e.getMessage());
//        }
//    }
//
//    public Page<Revenue> getRevenueList(Pageable pageable) {
//        return revenueRepository.findAll(pageable);
//    }
//}