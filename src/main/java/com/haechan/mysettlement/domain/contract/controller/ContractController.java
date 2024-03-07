package com.haechan.mysettlement.domain.contract.controller;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.contract.service.ContractService;
import com.haechan.mysettlement.global.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping("")
    public String index(Model model) {
        List<Contract> contractList = contractService.getContractList();
        model.addAttribute("contractList", contractList);
        return "/contract/contractIndex";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        contractService.uploadFile(file);

        return "redirect:/contract/list";
    }

    @GetMapping("/list")
    public String getContractList(Model model) {
        List<Contract> contractList = contractService.getContractList();
        model.addAttribute("contractList", contractList);
        return "/contract/contractList";
    }
}
