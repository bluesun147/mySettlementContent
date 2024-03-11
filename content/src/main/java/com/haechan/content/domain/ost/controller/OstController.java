package com.haechan.content.domain.ost.controller;

import com.haechan.content.domain.ost.dto.OstRegisterDto;
import com.haechan.content.domain.ost.service.OstService;
import com.haechan.content.global.feign.OstFeignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/ost")
@RequiredArgsConstructor
public class OstController {

    private final OstService ostService;

    // ost 등록
    @PostMapping("/")
    public void register(@RequestBody OstRegisterDto ostRegisterDto) {
        ostService.register(ostRegisterDto);
    }

    @GetMapping
    OstFeignResponse findOstById(@RequestParam(value = "memberId") Long memberId) {
        return ostService.findOstById(memberId);
    }
}
