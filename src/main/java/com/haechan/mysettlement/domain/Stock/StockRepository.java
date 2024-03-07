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

    /*
    2. Optimistic Lock (낙관적 락)
    실제 락 사용하지 않고 버전 이용해 락과 유사한 과정 갖는 논리적 락
        1. 2개 스레드에서 동시에 db 접근해 quantity 100, v1인 stock 조회
            SELECT * FROM stock WHERE id = 1
        2. 스레드1이 먼저 조회한 stock 업데이트 (q-1, v+1)
               UPDATE SET quantity=99, ver=ver+1 WHERE id=1 and v=1
        3. 스레드2가 조회한 stock 업데이트 할때 v=1인 stock 없으므로 예외 발생 (같은 stock 객체 접근 못함)
        4. 예외 잡아서 재조회하고 v2인 stock 업데이트
    1~3 과정은 어노테이션 통해서 자동 동작.
    4로직을 직접 구현해야 함.
    - 장점
        충돌 빈번하지 않으면, 별도 락 잡지 않으므로 성능 better
    - 단점
        업데이트 실패시 제시작 로직 직접 작성해야 함
        충돌 빈번하면 롤백 처리 해야 함. 성능 PL보다 bad

    */
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    Stock findByWithOptimisticLock(@Param("id") final Long id);
}
