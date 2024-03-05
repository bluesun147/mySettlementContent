package com.haechan.mysettlement.domain.Stock;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    /*
    1. Pessimistic Lock (비관적 락)
    exclusive lock 걸면 다른 트랜잭션 lock 해제 전에는 접근 X
    - 장점
        충돌 빈번하다면 롤백 횟수 줄일 수 있으므로 성능 better
        데이터 정합성 보장
    - 단점
        데이터 자체에서 락 잡으므로 동시성 떨어져 성능 저하 발생 가능성
        읽기 많은 db일 경우 손해 더 큼
        서로 필요할 경우 데드락 가능성
    */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    Stock findByWithPessimisticLock(@Param("id") final Long id);
}
