package com.haechan.mysettlement.global.batch;

import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.haechan.mysettlement.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

// 커스텀 ItemWriter
// 여러 Entity를 동시에 save 해야할때 만들어서 사용 가능
// https://jojoldu.tistory.com/339
// https://docs.spring.io/spring-batch/reference/readers-and-writers/custom.html
@Component
@RequiredArgsConstructor
public class SettlementListWriter implements ItemWriter<List<Settlement>> {

    private final SettlementRepository settlementRepository;

    @Override
    public void write(Chunk<? extends List<Settlement>> chunk) throws Exception {
        for (List<Settlement> settlementList : chunk) {
            settlementRepository.saveAll(settlementList); // 모든 정산 객체 저장
        }
    }
}