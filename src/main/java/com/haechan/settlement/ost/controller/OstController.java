package com.haechan.settlement.ost.controller;

import com.haechan.settlement.ost.dto.OstRegisterDto;
import com.haechan.settlement.ost.service.OstService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ost")
@RequiredArgsConstructor
public class OstController {

    private final OstService ostService;

    @PostMapping("/")
    public void register(@RequestBody OstRegisterDto ostRegisterDto) {
        ostService.register(ostRegisterDto);
    }
}
