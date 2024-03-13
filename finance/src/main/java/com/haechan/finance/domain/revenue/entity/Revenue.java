package com.haechan.finance.domain.revenue.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 수익 계산서

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계약서
//    @ManyToOne
//    @JoinColumn(name = "contractId")
//    private Contract contract;
    // 간접 참조
    private Long contractId;

    // 날짜
    @Column
    private LocalDateTime date;

    // 수익
    @Column
    private Double fee;

    // 생성 날짜
    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Revenue(Long contractId, LocalDateTime date, Double fee) {
        this.contractId = contractId;
        this.date = date;
        this.fee = fee;
    }

}
