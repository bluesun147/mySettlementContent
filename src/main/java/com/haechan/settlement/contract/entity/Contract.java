package com.haechan.settlement.contract.entity;

import com.haechan.settlement.distributor.entity.Distributor;
import com.haechan.settlement.producer.entity.Producer;
import com.haechan.settlement.singer.entity.Singer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 계약서

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제작사
    @ManyToOne
    @JoinColumn(name = "producerId")
    private Producer producer;

    // 유통사
    @ManyToOne
    @JoinColumn(name = "distributorId")
    private Distributor distributor;

    // 가창자
    @ManyToOne
    @JoinColumn(name = "singerId")
    private Singer singer;

    // 제작사 퍼센트
    @Column
    private Double producerPercent;

    // 유통사 퍼센트
    @Column
    private Double distributorPercent;

    // 가창자 퍼센트
    @Column
    private Double singerPercent;


    // 정산 대상 id
    @Column
    private Long memberId;

    // 시작일
    @Column
    private LocalDateTime startDate;

    // 종료일
    @Column
    private LocalDateTime endDate;

    // 업데이트 날짜
    @Column
    private LocalDateTime updateDate;

    @Builder
    public Contract(Producer producer, Distributor distributor, Singer singer,
                    Double producerPercent, Double distributorPercent, Double singerPercent,
                    Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        this.producer = producer;
        this.distributor = distributor;
        this.singer = singer;
        this.producerPercent = producerPercent;
        this.distributorPercent = distributorPercent;
        this.singerPercent = singerPercent;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
