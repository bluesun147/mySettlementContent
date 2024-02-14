package com.haechan.settlement.revenue.entity;

import com.haechan.settlement.distributor.entity.Distributor;
import com.haechan.settlement.ost.entity.Ost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 수익 계산서

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 유통사
    @ManyToOne
    @JoinColumn(name = "distributorId")
    private Distributor distributor;

    // ost
    @ManyToOne
    @JoinColumn(name = "ostId")
    private Ost ost;

    // 날짜
    @Column
    private LocalDateTime date;

    // 수익
    @Column
    private Double fee;

    @Builder
    public Revenue(Distributor distributor, Ost ost, LocalDateTime date, Double fee) {
        this.distributor = distributor;
        this.ost = ost;
        this.date = date;
        this.fee = fee;
    }

}
