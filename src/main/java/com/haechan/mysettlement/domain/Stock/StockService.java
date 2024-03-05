package com.haechan.mysettlement.domain.Stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// https://thalals.tistory.com/370

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    /*
    synchronized 없으면 Race Condition 발생 가능!
    하나에 스레드만 접근 가능
    멀티스레드 환경에서 스레드간 데이터 동기화 위해 사용
    현재 데이터 사용하고 있는 해당 스레드 제외하고, 나머지 스레드들 이전 스레드 종료까지 접근 막음
    But! 하나의 프로세스 안에서만 보장 (서버 2대 이상이면 데이터 접근 막을 수 없음)
    */
    public synchronized void decrease(final Long id, final Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}