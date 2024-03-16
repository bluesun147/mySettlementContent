package com.haechan.content.domain.stock.controller;

import com.haechan.content.domain.stock.entity.Stock;
import com.haechan.content.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/content/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    private static final int pageSize = 3;

    @GetMapping("/list")
    public String index(Model model, @PageableDefault(size = pageSize, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        List<Long> stockIdList = stockService.getAllStock().stream().map(Stock::getId).collect(Collectors.toList());
        List<String> changeTypeList = Arrays.asList("PLUS", "MINUS");

        model.addAttribute("stockIdList", stockIdList);
        model.addAttribute("changeTypeList", changeTypeList);


        Page<Stock> stockList = stockService.getStockList(pageable);
        model.addAttribute("stockList", stockList);

        List<Integer> pageIndex = new ArrayList<>();
        for (int i=0; i<stockList.getTotalPages(); i++) {
            pageIndex.add(i);
        }

        model.addAttribute("pageIndex", pageIndex);
        return "/stock/stockIndex";
    }

    @GetMapping("/change")
    public String change(
            @RequestParam("stockId") Long stockId,
            @RequestParam("amount") Long amount,
            @RequestParam("changeType") String changeType,
            @PageableDefault(size = pageSize, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("stockId = {}", stockId);
        log.info("amount = {}", amount);
        log.info("changeType = {}", changeType);

        stockService.change(stockId, amount, changeType);

        log.info("change done");

        return "redirect:list?page=0";
    }
}
