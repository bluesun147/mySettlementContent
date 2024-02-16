package com.haechan.mysettlement.domain.contract.controller;

import com.haechan.mysettlement.domain.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/")
    public void uploadFile(@RequestParam(name="file") MultipartFile file) {
        contractService.uploadFile(file);
    }
}
