package com.haechan.settlement.revenue.entity;

import com.haechan.settlement.distributor.entity.Distributor;
import com.haechan.settlement.ost.entity.Ost;
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

    // ost
    @ManyToOne
    @JoinColumn(name = "ostId")
    private Ost ost;

    // 유통사
    @ManyToOne
    @JoinColumn(name = "distributorId")
    private Distributor distributor;

    // 날짜
    @Column
    private LocalDateTime date;

    // 수익
    @Column
    private Double fee;

    @Builder
    public Revenue(Ost ost, Distributor distributor, LocalDateTime date, Double fee) {
        this.ost = ost;
        this.distributor = distributor;
        this.date = date;
        this.fee = fee;
    }

}
