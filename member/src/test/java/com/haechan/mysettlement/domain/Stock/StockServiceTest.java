package com.haechan.mysettlement.domain.Stock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void 동시100개요청_실패() throws InterruptedException {

        List<Stock> all = stockRepository.findAll();

        System.out.println("all : "+all.get(0));

        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i=0; i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    // latch 숫자 감소
                    latch.countDown();
                }
            });
        }

        // latch 숫자 0 될때까지 기다림
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);

        // Race Condition 때문에 0 안나옴!
        assertThat(stock.getQuantity()).isNotEqualTo(0L);
    }

    @Test
    public void 동시100개요청_synchronized() throws InterruptedException {

        List<Stock> all = stockRepository.findAll();

        System.out.println("all : "+all.get(0));

        int threadCount = 100;

        // 병렬 작업 시 여러 작업 호율적 처리 위해 제공되는 자바 라이브러리
        // threadPool 쉽게 구성하고 task 실행, 관리할 수 있음
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 어떤 스레드가 다른 스레드 작업 완료까지 기다리게 해주는 클래스
        // 100번 작업 완료까지 기다림
        // latch 개수 100개로 지정
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i=0; i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseSynchronized(1L, 1L);
                } finally {
                    // latch 숫자 감소
                    latch.countDown();
                }
            });
        }

        // latch 숫자 0 될때까지 기다림
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);

        assertThat(stock.getQuantity()).isEqualTo(0L);
    }

    @Test
    public void 동시100개요청_pessimistic() throws InterruptedException {

        List<Stock> all = stockRepository.findAll();

        System.out.println("all : "+all.get(0));

        int threadCount = 100;

        // 병렬 작업 시 여러 작업 호율적 처리 위해 제공되는 자바 라이브러리
        // threadPool 쉽게 구성하고 task 실행, 관리할 수 있음
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 어떤 스레드가 다른 스레드 작업 완료까지 기다리게 해주는 클래스
        // 100번 작업 완료까지 기다림
        // latch 개수 100개로 지정
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i=0; i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreasePessimistic(1L, 1L);
                } finally {
                    // latch 숫자 감소
                    latch.countDown();
                }
            });
        }

        // latch 숫자 0 될때까지 기다림
        latch.await();

//         Stock stock = stockRepository.findByWithPessimisticLock(1L);
        Stock stock = stockRepository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);

        assertThat(stock.getQuantity()).isEqualTo(0L);
    }

    @Test
    public void 동시100개요청_optimistic() throws InterruptedException {

        List<Stock> all = stockRepository.findAll();

        System.out.println("all : "+all.get(0));

        int threadCount = 100;

        // 병렬 작업 시 여러 작업 호율적 처리 위해 제공되는 자바 라이브러리
        // threadPool 쉽게 구성하고 task 실행, 관리할 수 있음
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 어떤 스레드가 다른 스레드 작업 완료까지 기다리게 해주는 클래스
        // 100번 작업 완료까지 기다림
        // latch 개수 100개로 지정
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i=0; i<threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // 동시 접근 시 sleep 후 다시 조회
                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    // latch 숫자 감소
                    latch.countDown();
                }
            });
        }

        // latch 숫자 0 될때까지 기다림
        // 다른 스레드에서 수행중인 작업 완료까지 기다림
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);

        assertThat(stock.getQuantity()).isEqualTo(0L);
    }
}