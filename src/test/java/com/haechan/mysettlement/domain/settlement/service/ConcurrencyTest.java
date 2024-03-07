package com.haechan.mysettlement.domain.settlement.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConcurrencyTest {

    @Autowired
    private SettlementService settlementService;

    /*
    // ThreadLocal 사용 전
    main start
    (save) saved value : nameStore = null, to be saved : name = userA
    (save) saved value : nameStore = userA, to be saved : name = userB
    (get) saved value = userB
    (get) saved value = userB
    main exit

    // ThreadLocal 사용
    main start
    (save) saved value : nameStore = null, to be saved : name = userA
    (save) saved value : nameStore = null, to be saved : name = userB
    (get) saved value = userA
    (get) saved value = userB
    main exit
     */
    @Test
    void concurrency_issue_ThreadLocal() {
        System.out.println("main start");
        Runnable userA = () -> {
            settlementService.saveAndFind("userA");
        };

        Runnable userB = () -> {
            settlementService.saveAndFind("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");

        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        // Runnable 로직 실행
        threadA.start();
        // 동시성 문제 발생
        // 저장에 1초 걸림. but threadA가 로직 실행중인데 끝나기 전에 threaqB 개입한 상황
        // threadA는 userA 조회하지만 userB 값 조회 됨.
        settlementService.sleep(100);
        threadB.start();

        // 메인 스레드 종료 대기
        settlementService.sleep(3000);

        System.out.println("main exit");
    }
}