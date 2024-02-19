package com.haechan.mysettlement.domain.revenue.repository;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.contract.repository.ContractRepository;
import com.haechan.mysettlement.domain.revenue.entity.QRevenue;
import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestConfiguration
class TestQueryDslConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}


@DataJpaTest
@Import(TestQueryDslConfig.class)
@ExtendWith(MockitoExtension.class)
// h2 임베디드 모드 설정 말고 운영 DB 사용하겠다는 뜻
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RevenueRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    private ContractRepository contractRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void init() {
        em = testEntityManager.getEntityManager();
    }

    @Test
    @DisplayName("계약서로 수익 객체 찾기")
    void findByContractTest() {
        JPAQuery<Revenue> query = new JPAQuery<>(em);
        QRevenue qRevenue = new QRevenue("revenue");

        Contract contract = contractRepository.findById(1L).orElseThrow();

        List<Revenue> revenueList = query.from(qRevenue)
                .where(qRevenue.contract.eq(contract))
                .fetch();

        assertThat(revenueList.get(0).getContract()).isEqualTo(contract);
        assertThat(revenueList.size()).isEqualTo(1);
    }
}