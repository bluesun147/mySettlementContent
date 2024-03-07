package com.haechan.mysettlement.domain.revenue.controller;

import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.revenue.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("")
    public String index(Model model) {
        List<Revenue> revenueList = revenueService.getRevenueList();
        model.addAttribute("revenueList", revenueList);
        return "/revenue/revenueIndex";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name="file") MultipartFile file) {
        revenueService.uploadFile(file);

        return "redirect:/revenue/list";
    }

    @GetMapping("/list")
    public String getRevenueList(Model model) {
        List<Revenue> revenueList = revenueService.getRevenueList();
        model.addAttribute("revenueList", revenueList);
        return "/revenue/revenueList";
    }
}
