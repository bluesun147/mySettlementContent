package com.haechan.finance.global.feign;

import com.haechan.feign.dto.SingerFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// name : 통신할 서비스의 Eureka 등록 이름
// path : RequestMapping의 value와 동일'
// FeignClient끼리 name 같으면 contextId로 구분해야 함
@Component
@FeignClient(name = "member-service", contextId = "feignClientForSinger", path = "/member/singer")
public interface SingerFeignClient {

    // FeintClient 설정해주면 마치 자신의 API 인것처럼 정의 가능. 세부 구현은 x
    @GetMapping
    SingerFeignResponse findSingerById(@RequestParam(value = "memberId") Long memberId);

    @GetMapping("/list")
    List<SingerFeignResponse> findAll();
}