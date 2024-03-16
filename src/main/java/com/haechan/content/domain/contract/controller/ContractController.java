package com.haechan.content.domain.contract.controller;

import com.haechan.content.domain.contract.entity.Contract;
import com.haechan.content.domain.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/content/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private static final int pageSize = 3;

    @GetMapping("/list")
    public String index(Model model, @PageableDefault(size = pageSize, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Contract> contractList = contractService.getContractList(pageable);
        model.addAttribute("contractList", contractList);

        List<Integer> pageIndex = new ArrayList<>();
        for (int i=0; i<contractList.getTotalPages(); i++) {
            pageIndex.add(i);
        }

        model.addAttribute("pageIndex", pageIndex);
        return "/contract/contractIndex";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        contractService.uploadFile(file);

        return "redirect:/content/contract/list?page=0";
    }
}
