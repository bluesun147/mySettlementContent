package com.haechan.member.domain.distributor.controller;

import com.haechan.member.domain.distributor.dto.DistributorDto;
import com.haechan.member.domain.distributor.feign.DistributorFeignResponse;
import com.haechan.member.domain.distributor.service.DistributorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/distributor")
@RequiredArgsConstructor
public class DistributorController {

    private final DistributorService distributorService;

    @PostMapping("/")
    public void register(@RequestBody DistributorDto distributorDto) {
        distributorService.register(distributorDto);
    }

    @GetMapping
    public DistributorFeignResponse findDistributorById(@RequestParam(value = "memberId") Long memberId) {
        return distributorService.findDistributorById(memberId);
    }
}
