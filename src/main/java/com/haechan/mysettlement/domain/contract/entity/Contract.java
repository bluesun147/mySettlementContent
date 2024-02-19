package com.haechan.mysettlement.domain.contract.entity;

import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 계약서
// ost 유통 계약서

@ToString
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Contract {
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

    // 생성 날짜
    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    // 업데이트 날짜
    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Contract(Ost ost, Distributor distributor,
                    Double producerPercent, Double singerPercent,
                    LocalDateTime startDate, LocalDateTime endDate) {
        this.ost = ost;
        this.distributor = distributor;
        this.producerPercent = producerPercent;
        this.singerPercent = singerPercent;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
