package com.haechan.content.domain.contract.entity;

import com.haechan.content.domain.ost.entity.Ost;
import com.haechan.content.global.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 계약서
// ost 유통 계약서

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Contract extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ost
    @ManyToOne
    @JoinColumn(name = "ostId")
    private Ost ost;

    // 유통사
//    @ManyToOne
//    @JoinColumn(name = "distributorId")
//    private Distributor distributor;

    // 간접 참조 (msa)
    private Long distributorId;

    // 제작사 퍼센트
    @Column
    private Double producerPercent;

    // 가창자 퍼센트
    @Column
    private Double singerPercent;

    // 시작일
    @Column
    private LocalDateTime startDate;

    // 종료일
    @Column
    private LocalDateTime endDate;

    @Builder
    public Contract(Ost ost, Long distributorId,
                    Double producerPercent, Double singerPercent,
                    LocalDateTime startDate, LocalDateTime endDate) {
        this.ost = ost;
        this.distributorId = distributorId;
        this.producerPercent = producerPercent;
        this.singerPercent = singerPercent;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
