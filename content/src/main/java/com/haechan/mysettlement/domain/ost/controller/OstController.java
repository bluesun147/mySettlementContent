package com.haechan.mysettlement.domain.ost.controller;

import com.haechan.mysettlement.domain.ost.dto.OstRegisterDto;
import com.haechan.mysettlement.domain.ost.service.OstService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ost")
@RequiredArgsConstructor
public class OstController {

    private final OstService ostService;

    // ost 등록
    @PostMapping("/")
    public void register(@RequestBody OstRegisterDto ostRegisterDto) {
        ostService.register(ostRegisterDto);
    }
}
