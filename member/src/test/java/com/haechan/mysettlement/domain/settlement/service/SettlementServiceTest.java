package com.haechan.mysettlement.domain.settlement.service;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.drama.entity.Drama;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import com.haechan.mysettlement.domain.producer.entity.Producer;
import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.revenue.repository.RevenueRepository;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.haechan.mysettlement.domain.settlement.repository.SettlementRepository;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.haechan.mysettlement.domain.settlement.dto.MemberType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    // 가짜 객체 만들어 반환
    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private RevenueRepository revenueRepository;

    // @Mock으로 만든 가짜 객체 자동으로 주입
    // 가짜 리포지토리 객체 주입
    @InjectMocks
    private SettlementService settlementService;

    @Test
    @DisplayName("각 멤버 순차적 정산")
    void calculate_success() {
        // Given
        LocalDateTime dateTime = LocalDateTime.now();

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

        Distributor distributor1 = Distributor.builder()
                .name("distributor1")
                .percent(50.0)
                .build();
        ReflectionTestUtils.setField(distributor1, "id", 1L);

        Distributor distributor2 = Distributor.builder()
                .name("distributor2")
                .percent(40.0)
                .build();
        ReflectionTestUtils.setField(distributor2, "id", 2L);

        Contract contract1 = Contract.builder()
                .ost(ost)
                .distributor(distributor1)
                .producerPercent(50.0)
                .singerPercent(10.0)
                .startDate(dateTime)
                .endDate(dateTime)
                .build();
        ReflectionTestUtils.setField(contract1, "id", 1L);

        Contract contract2 = Contract.builder()
                .ost(ost)
                .distributor(distributor2)
                .producerPercent(50.0)
                .singerPercent(10.0)
                .startDate(dateTime)
                .endDate(dateTime)
                .build();
        ReflectionTestUtils.setField(contract2, "id", 2L);


        List<Revenue> revenueList = Arrays.asList(
                new Revenue(contract1, dateTime, 100.0),
                new Revenue(contract2, dateTime, 200.0)
        );

        LocalDate date = dateTime.toLocalDate();
        Mockito.when(revenueRepository.findByDate(date)).thenReturn(revenueList);

        // When
        settlementService.calculate(date);

        // Then
        verify(revenueRepository, times(1)).findByDate(date);
        verify(settlementRepository, times(revenueList.size()*4)).save(any(Settlement.class));

        // 추가적인 검증 로직 추가 가능 (정산 결과 확인, 저장된 데이터 비교 등)
        // fee 값의 합이 원래 fee 값과 일치하는지
        // 예상 Settlement 객체 리스트 생성
        List<Settlement> expectedSettlements = Arrays.asList(
                // 유통사1
                new Settlement(contract1, DISTRIBUTOR, distributor1.getId(), dateTime, 50.0),
                // 가창자1
                new Settlement(contract1, SINGER, singer.getId(), dateTime, 5.0),
                // 제작자1
                new Settlement(contract1, PRODUCER, producer.getId(), dateTime, 22.5),
                // 본사1
                new Settlement(contract1, COMPANY, 0L, dateTime, 22.5),

                // 유통사2
                new Settlement(contract2, DISTRIBUTOR, distributor2.getId(), dateTime, 80.0),
                // 가창자2
                new Settlement(contract2, SINGER, singer.getId(), dateTime, 12.0),
                // 제작자2
                new Settlement(contract2, PRODUCER, producer.getId(), dateTime, 54.0),
                // 본사2
                new Settlement(contract2, COMPANY, 0L, dateTime, 54.0)

        );

        // 저장된 Settlement 객체 리스트와 예상 객체 리스트 비교
        verify(settlementRepository, times(expectedSettlements.size())).save(any(Settlement.class));
        // ArgumentCaptor: 메소드에 들어가는 인자갑 중간에 가로채서 검증
        ArgumentCaptor<Settlement> settlementCaptor = ArgumentCaptor.forClass(Settlement.class);

        // verify 지우면 밑에도 실행 안됨.
        // verify에 적힌 메서드의 인자로 들어갔는지 테스트 하는것
        verify(settlementRepository, times(expectedSettlements.size())).save(settlementCaptor.capture());
        List<Settlement> actualSettlements = settlementCaptor.getAllValues();

        for (Settlement settlement : actualSettlements) {
            System.out.println("actual settlement = " + settlement);
        }

        assertEquals(expectedSettlements.size(), actualSettlements.size());

        for (int i = 0; i < expectedSettlements.size(); i++) {
            Settlement expectedSettlement = expectedSettlements.get(i);
            Settlement actualSettlement = actualSettlements.get(i);

            // 필드별로 비교
            assertEquals(expectedSettlement.getContract(), actualSettlement.getContract());
            assertEquals(expectedSettlement.getType(), actualSettlement.getType());
            assertEquals(expectedSettlement.getMemberId(), actualSettlement.getMemberId());
            // 초 미세하게 달라져서 date로 변환
            assertEquals(expectedSettlement.getSettleDate().toLocalDate(), actualSettlement.getSettleDate().toLocalDate());
            assertEquals(expectedSettlement.getFee(), actualSettlement.getFee());
        }
    }

    @Test
    void mocking() {
        ArrayList<String> mockList = mock(ArrayList.class);

        System.out.println(mockList.get(0));  // null
        System.out.println(mockList.size());  // 0

        mockList.add("Test String 1");  // 영향 없음
        mockList.add("Test String 2");

        System.out.println(mockList.size());  // 0

        when(mockList.size()).thenReturn(5);
        System.out.println(mockList.size());  // 5
    }

    @Test
    public void spying() {
        ArrayList<String> spyList = spy(ArrayList.class);

//        System.out.println(spyList.get(0));  // IndexOutOfBoundsException
        System.out.println(spyList.size());  // 0

        spyList.add("Test String 1");
        spyList.add("Test String 2");

        System.out.println(spyList.size());  // 2
        System.out.println(spyList.get(0));  // Test String 1
        System.out.println(spyList.get(1));  // Test String 2

        when(spyList.size()).thenReturn(10);  // 행동 재정의
        System.out.println(spyList.size());  // 10
    }


}