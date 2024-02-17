package com.haechan.mysettlement.domain.settlement.controller;

import com.haechan.mysettlement.domain.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/settlement")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/{date}")
    public void calculate(@PathVariable(name="date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        settlementService.calculate(date);
    }
}
