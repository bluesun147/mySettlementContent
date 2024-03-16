package com.haechan.content.domain.stock.service;

import com.haechan.content.domain.stock.entity.Stock;
import com.haechan.content.domain.stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// https://thalals.tistory.com/370
// https://ksh-coding.tistory.com/125
// https://velog.io/@coconenne/스프링으로-알아보는-동시성-이슈

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public void test() {
        System.out.println("wewe");
    }

    public void decrease(final Long id, final Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }

    /*
    synchronized 없으면 Race Condition 발생 가능!
    하나에 스레드만 접근 가능
    멀티스레드 환경에서 스레드간 데이터 동기화 위해 사용
    현재 데이터 사용하고 있는 해당 스레드 제외하고, 나머지 스레드들 이전 스레드 종료까지 접근 막음
    But! 하나의 프로세스 안에서만 보장 (서버 2대 이상이면 데이터 접근 막을 수 없음)
    */
    public synchronized void decreaseSynchronized(final Long id, final Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }

    @Transactional
    public void decreasePessimistic(final Long id, final Long quantity) {
        Stock stock = stockRepository.findByWithPessimisticLock(id);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }

    @Transactional
    public void decreaseOptimistic(final Long id, final Long quantity) {
        Stock stock = stockRepository.findByWithOptimisticLock(id);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }

    public Page<Stock> getStockList(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Transactional
    public void change(Long stockId, Long amount, String changeType) {
        Stock stock = stockRepository.findByWithPessimisticLock(stockId);
        if (changeType.equals("PLUS")) {
            stock.increase(amount);
        } else if (changeType.equals("MINUS")) {
            stock.decrease(amount);
        }
    }
}