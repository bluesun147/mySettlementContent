package com.haechan.mysettlement.domain.revenue.repository;

import com.haechan.mysettlement.domain.config.TestQueryDslConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.haechan.mysettlement.domain.revenue.entity.QRevenue.revenue;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

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

    @Test
    public void testFindByThisMonth() {
        // 테스트할 날짜
        LocalDate testDate = LocalDate.of(2024, Month.MARCH, 1);

        // 페이징 정보 생성
        Pageable pageable = PageRequest.of(0, 10);

        // findByDate2 메서드 호출
        Page<Revenue> resultPage = revenueRepository.findByThisMonth(testDate, pageable);

        resultPage.get().forEach(System.out::println);

        // 결과 검증
        assertNotNull(resultPage); // 결과 페이지가 null이 아닌지 확인
        assertFalse(resultPage.isEmpty()); // 결과 페이지가 비어있지 않은지 확인
        assertEquals(2, resultPage.getContent().size()); // 페이지 크기가 10인지 확인 (페이징 정보에 따라 다를 수 있음)
    }

    @Test
    public void testFindByDateAfter() {
        // 테스트할 기준 시간 설정
        LocalDateTime testDateTime = LocalDateTime.of(2024, Month.MARCH, 1, 0, 0);

        // 페이징 정보 생성
        Pageable pageable = PageRequest.of(0, 10);

        // findByDateAfter 메서드 호출
        Page<Revenue> resultPage = revenueRepository.findByDateAfter(testDateTime, pageable);

        // 결과 검증
        assertNotNull(resultPage); // 결과 페이지가 null이 아닌지 확인
        assertFalse(resultPage.isEmpty()); // 결과 페이지가 비어있지 않은지 확인
        assertEquals(2, resultPage.getContent().size());
    }

    @Test

    public void testPageDefault() {

        LocalDate testDate = LocalDate.of(2024, Month.MARCH, 1);

        // 페이징 정보 생성
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Revenue> result = revenueRepository.findByThisMonth(testDate, pageable);

        System.out.println(result);

        System.out.println("-------------------------------------------");

        // 총 몇 페이지
        System.out.println("Total Page : " + result.getTotalPages());

        // 전체 개수
        System.out.println("Total Count : " + result.getTotalElements());

        // 현재 페이지 번호 0부터 시작
        System.out.println("Page Number : " + result.getNumber());

        // 페이지당 데이터 개수
        System.out.println("Page Size : " + result.getSize());

        // 다음 페이지 존재 여부
        System.out.println("has next page ? " + result.hasNext());

        // 시작 페이지(0) 여부
        System.out.println("first page ? " + result.isFirst());

        System.out.println("-------------------------------------------");

        for (Revenue revenue : result.getContent()) {
            System.out.println(revenue);
        }
    }

    @Test
    public void 페이징_테스트() {
        LocalDate date = LocalDate.of(2024, Month.MARCH, 1); // 테스트할 날짜 지정
        Pageable pageable = PageRequest.of(0, 10); // 첫 페이지, 페이지당 10개 항목 조회

        // Page 객체 조회
        Page<Revenue> revenuePage = revenueRepository.findByThisMonth(date, pageable);

        // 조회된 항목 수 검증
        assertThat(revenuePage.getContent().size()).isEqualTo(10); // 첫 페이지에 10개 항목이 있는지 확인

        // 전체 항목 수 검증 (count 쿼리 결과와 비교)
        JPAQuery<Long> countQuery = queryFactory.select(revenue.count())
                .from(revenue)
                .where(revenue.date.year().eq(date.getYear()))
                .where(revenue.date.month().eq(date.getMonthValue()));
        long totalCount = countQuery.fetchOne();
        assertThat(revenuePage.getTotalElements()).isEqualTo(totalCount);

        // 페이징 정보 검증
        assertThat(revenuePage.getPageable().getPageNumber()).isEqualTo(0);
        // offset, limit 지정 안하면 테스트 실패!! 전체 다 불러옴.
        assertThat(revenuePage.getPageable().getPageSize()).isEqualTo(10);
    }
}