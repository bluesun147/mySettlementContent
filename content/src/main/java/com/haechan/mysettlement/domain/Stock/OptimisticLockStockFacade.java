package com.haechan.mysettlement.domain.Stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final StockService stockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                // 낙관적 락
                stockService.decreaseOptimistic(id, quantity);
                break;
            } catch (Exception e) {
                // 단계 4 로직 구현한 것.
                // 같은 객체 접근 시 sleep 후 다시 조회
                Thread.sleep(50);
            }
        }
    }
}
