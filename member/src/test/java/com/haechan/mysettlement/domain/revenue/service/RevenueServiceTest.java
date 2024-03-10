package com.haechan.mysettlement.domain.revenue.service;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.contract.repository.ContractRepository;
import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.drama.entity.Drama;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import com.haechan.mysettlement.domain.producer.entity.Producer;
import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RevenueServiceTest {

    @Mock
    private ContractRepository contractRepository;


    /*
    엑셀 파일용 컬럼은 ostId, distributorId, fee, date
    저장 테이블 컬럼은 contractId, fee, date
     */
    @Test
    @DisplayName("수익 엑셀 파일 객체와 저장되는 객체 일치하는지 테스트")
    void excel_table_matches() {

        // given
        // 엑셀 파일 저장 객체
        Long ostId = 1L;
        Long distributorId = 1L;
        Double fee = 100.0;
        LocalDateTime date = LocalDateTime.now();

        Drama drama = Drama.builder()
                .title("drama1")
                .build();

        Producer producer = Producer.builder()
                .name("producer1")
                .build();

        Singer singer = Singer.builder()
                .name("singer1")
                .build();

        Ost ost = Ost.builder()
                .drama(drama)
                .producer(producer)
                .singer(singer)
                .title("ost1")
                .build();
        ReflectionTestUtils.setField(ost, "id", ostId);

        Distributor distributor = Distributor.builder()
                .name("distributor1")
                .percent(50.0)
                .build();
        ReflectionTestUtils.setField(distributor, "id", distributorId);

        Contract savedContract = Contract.builder()
                .ost(ost)
                .distributor(distributor)
                .producerPercent(50.0)
                .singerPercent(10.0)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        // ost와 distributor로 저장되어 있던 contract 찾기
        when(contractRepository.findByOstAndDistributor(ost, distributor)).thenReturn(Optional.of(savedContract));

        // when
        Contract contract = contractRepository.findByOstAndDistributor(ost, distributor).orElseThrow();
        Revenue revenue = Revenue.builder()
                .contract(contract)
                .date(date)
                .fee(fee)
                .build();

        // then
        assertThat(revenue.getContract().getOst()).isEqualTo(ost);
        assertThat(revenue.getContract().getDistributor()).isEqualTo(distributor);
    }
}