package com.haechan.mysettlement.domain.contract.entity;

import com.haechan.mysettlement.domain.distributor.entity.Distributor;
import com.haechan.mysettlement.domain.producer.entity.Producer;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 계약서

@EntityListeners(AuditingEntityListener.class)
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
    public Contract(Producer producer, Distributor distributor, Singer singer,
                    Double producerPercent, Double singerPercent,
                    LocalDateTime startDate, LocalDateTime endDate) {
        this.producer = producer;
        this.distributor = distributor;
        this.singer = singer;
        this.producerPercent = producerPercent;
        this.singerPercent = singerPercent;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
