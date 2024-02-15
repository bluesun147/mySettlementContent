package com.haechan.settlement.revenue.controller;

import com.haechan.settlement.revenue.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @PostMapping("/")
    public void uploadFile(@RequestParam(name="file") MultipartFile file) {
        revenueService.uploadFile(file);
    }
}
