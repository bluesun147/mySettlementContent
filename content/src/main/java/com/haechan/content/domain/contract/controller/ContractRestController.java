package com.haechan.content.domain.contract.controller;

import com.haechan.content.domain.contract.service.ContractService;
import com.haechan.content.global.feign.ContractFeignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/contract")
@RequiredArgsConstructor
public class ContractRestController {

    private final ContractService contractService;


    @GetMapping
    ContractFeignResponse findByOstIdAndDistributorId(@RequestParam(value = "ostMemberId") Long ostMemberId, @RequestParam(value = "distributorMemberId") Long distributorMemberId) {
        return contractService.findContractById(ostMemberId, distributorMemberId);
    }
}
