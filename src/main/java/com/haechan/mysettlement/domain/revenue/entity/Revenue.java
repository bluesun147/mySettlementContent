package com.haechan.mysettlement.domain.revenue.entity;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 수익 계산서

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계약서
    @ManyToOne
    @JoinColumn(name = "contractId")
    private Contract contract;

    // 날짜
    @Column
    private LocalDateTime date;

    // 수익
    @Column
    private Double fee;

    @Builder
    public Revenue(Contract contract, LocalDateTime date, Double fee) {
        this.contract = contract;
        this.date = date;
        this.fee = fee;
    }

}
