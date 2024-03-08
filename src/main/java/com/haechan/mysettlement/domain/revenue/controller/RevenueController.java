package com.haechan.mysettlement.domain.revenue.controller;

import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.revenue.service.RevenueService;
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

//@RestController
@Controller
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    private static final int pageSize = 3;

    @GetMapping("")
    public String index(Model model, @PageableDefault(size = pageSize, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Revenue> revenueList = revenueService.getRevenueList(pageable);
        model.addAttribute("revenueList", revenueList);

        List<Integer> pageIndex = new ArrayList<>();
        for (int i=0; i<revenueList.getTotalPages(); i++) {
            pageIndex.add(i);
        }

        model.addAttribute("pageIndex", pageIndex);
        return "/revenue/revenueIndex";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name="file") MultipartFile file) {
        revenueService.uploadFile(file);

        return "redirect:/revenue?page=0";
    }
}
