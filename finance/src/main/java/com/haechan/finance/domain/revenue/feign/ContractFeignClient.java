package com.haechan.finance.domain.revenue.feign;

import com.haechan.feign.dto.ContractFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// name : 통신할 서비스의 Eureka 등록 이름
// path : RequestMapping의 value와 동일
@FeignClient(name = "content-service", contextId = "feignClientForContract", path = "/content/contract")
public interface ContractFeignClient {

    // FeintClient 설정해주면 마치 자신의 API 인것처럼 정의 가능. 세부 구현은 x
//    @GetMapping
//    OstFeignResponse findContractById(@RequestParam(value = "memberId") Long memberId);

    @GetMapping
    ContractFeignResponse findByOstIdAndDistributorId(@RequestParam(value = "ostMemberId") Long ostMemberId, @RequestParam(value = "distributorMemberId") Long distributorMemberId);
}