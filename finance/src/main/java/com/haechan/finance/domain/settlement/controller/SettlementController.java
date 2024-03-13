package com.haechan.finance.domain.settlement.controller;

import com.haechan.feign.dto.DistributorFeignResponse;
import com.haechan.feign.dto.ProducerFeignResponse;
import com.haechan.feign.dto.SingerFeignResponse;
import com.haechan.finance.domain.settlement.dto.MemberType;
import com.haechan.finance.domain.settlement.entity.Settlement;
import com.haechan.finance.global.feign.DistributorFeignClient;
import com.haechan.finance.global.feign.SingerFeignClient;
import com.haechan.finance.global.feign.ProducerFeignClient;
import com.haechan.finance.domain.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.haechan.finance.domain.settlement.dto.MemberType.*;

@Slf4j
//@RestController
@Controller
@RequestMapping("/finance/settlement")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;
    private final DistributorFeignClient distributorFeignClient;
    private final SingerFeignClient singerFeignClient;
    private final ProducerFeignClient producerFeignClient;

    private static final int pageSize = 3;

    @GetMapping("")
    public String index(Model model, @PageableDefault(size = pageSize, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

         Page<Settlement> settlementList = settlementService.getSettlementList(pageable);

        model.addAttribute("settlementList", settlementList);

        List<Integer> pageIndex = new ArrayList<>();
        for (int i=0; i<settlementList.getTotalPages(); i++) {
            pageIndex.add(i);
        }

        model.addAttribute("pageIndex", pageIndex);

        List<Integer> years = Arrays.asList(2020, 2021, 2022, 2023, 2024, 2025);
        List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        List<MemberType> types = Arrays.asList(DISTRIBUTOR, SINGER, PRODUCER, COMPANY);
        List<String> memberIds = Arrays.asList("멤버선택");

        model.addAttribute("years", years);
        model.addAttribute("months", months);
        model.addAttribute("types", types);
        model.addAttribute("memberIds", memberIds);

//        List<DistributorHtmlSelectDto> distributorList = distributorService.getDistributorList();
//        List<Long> distributorIdList = distributorList.stream().map(DistributorHtmlSelectDto::getId).toList();

        List<DistributorFeignResponse> distributorFeignList = distributorFeignClient.findAll();
        List<Long> distributorIdList = distributorFeignList.stream().map(DistributorFeignResponse::getDistributorId).toList();

        List<SingerFeignResponse> singerFeignList = singerFeignClient.findAll();
        List<Long> singerIdList = singerFeignList.stream().map(SingerFeignResponse::getSingerId).toList();

        List<ProducerFeignResponse> producerFeignList = producerFeignClient.findAll();
        List<Long> producerIdList = producerFeignList.stream().map(ProducerFeignResponse::getProducerId).toList();

        model.addAttribute("distributorIdList", distributorIdList);
        model.addAttribute("singerIdList", singerIdList);
        model.addAttribute("producerIdList", producerIdList);

        return "/settlement/settlementIndex";
    }

    // 특정월 특정 유통사의 수익
    @GetMapping("/member")
    public String getDistributorSettlement(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("type") MemberType type,
            @RequestParam("memberId") Long memberId,
            Model model,
            @PageableDefault(size = pageSize, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Settlement> settlementList = settlementService.getSettlementListByDateAndMember(year, month, type, memberId, pageable);
        model.addAttribute("settlementList", settlementList);

        List<Integer> pageIndex = new ArrayList<>();
        for (int i=0; i<settlementList.getTotalPages(); i++) {
            pageIndex.add(i);
        }

        model.addAttribute("pageIndex", pageIndex);

        // 해당 월 정산금
        Double membersSettlement = settlementService.getMembersSettlement(year, month, type, memberId);
        log.info("membersSettlement = {}", membersSettlement);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("type", type);
        model.addAttribute("memberId", memberId);
        model.addAttribute("membersSettlement", membersSettlement);

        return "settlement/result";
    }
}