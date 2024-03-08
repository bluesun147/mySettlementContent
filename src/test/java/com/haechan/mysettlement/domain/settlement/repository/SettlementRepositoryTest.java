package com.haechan.mysettlement.domain.settlement.repository;

import com.haechan.mysettlement.domain.config.TestQueryDslConfig;
import com.haechan.mysettlement.domain.settlement.dto.MemberType;
import com.haechan.mysettlement.domain.settlement.entity.QSettlement;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.List;

import static com.haechan.mysettlement.domain.settlement.entity.QSettlement.settlement;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
@ExtendWith(MockitoExtension.class)
// h2 임베디드 모드 설정 말고 운영 DB 사용하겠다는 뜻
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SettlementRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void init() {
        em = testEntityManager.getEntityManager();
    }

    @Autowired
    private SettlementRepository settlementRepository;

    @Test
    @DisplayName("특정월 특정 멤버(유통사, 가창자, 제작사, 본사) 찾기")
    void findByTypeAndMemberIdAndSettleDate() {
        JPAQuery<Settlement> query = new JPAQuery<>(em);
        QSettlement qSettlement = new QSettlement("settlement");

        MemberType type = MemberType.DISTRIBUTOR;
        Long memberId = 1L;
        LocalDate date = LocalDate.now();

        List<Settlement> settlementList = query.from(qSettlement)
                .where(qSettlement.type.eq(type))
                .where(qSettlement.memberId.eq(memberId))
                .where(settlement.settleDate.year().eq(date.getYear()))
                .where(settlement.settleDate.month().eq(date.getMonthValue()))
                .fetch();

        assertThat(settlementList.size()).isEqualTo(3);
        assertThat(settlementList.get(0).getType()).isEqualTo(type);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Settlement> all = settlementRepository.findAll(pageable);

        System.out.println("all = " + all);

    }
}