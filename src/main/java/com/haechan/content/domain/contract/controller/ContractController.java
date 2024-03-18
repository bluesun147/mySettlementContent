package com.haechan.content.domain.contract.controller;

import com.haechan.content.domain.contract.entity.Contract;
import com.haechan.content.domain.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/content/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private static final int pageSize = 3;

    @GetMapping("/list")
    public String index(
            Model model,
            @PageableDefault(size = pageSize, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute("errorMessage") String errorMessage) {
        Page<Contract> contractList = contractService.getContractList(pageable);
        model.addAttribute("contractList", contractList);

        List<Integer> pageIndex = new ArrayList<>();
        for (int i=0; i<contractList.getTotalPages(); i++) {
            pageIndex.add(i);
        }

        log.debug("errorMessage = {}", errorMessage);

        model.addAttribute("errorMessage", errorMessage);

        model.addAttribute("pageIndex", pageIndex);
        return "/contract/contractIndex";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes re) {
        try {
            contractService.uploadFile(file);
            re.addFlashAttribute("errorMessage", "");
            return "redirect:/content/contract/list?page=0";
        } catch (RuntimeException e) {
            log.info("e.getMessage() = {}", e.getMessage());
            re.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/content/contract/list?page=0"+"&errorMessage=" + e.getMessage();
        }
    }
}
