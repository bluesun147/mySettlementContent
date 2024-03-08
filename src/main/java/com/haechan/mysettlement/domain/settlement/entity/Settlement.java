package com.haechan.mysettlement.domain.settlement.entity;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.settlement.dto.MemberType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 정산

@ToString
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계약서
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId")
    private Contract contract;

    // 정산대상 타입
    // 추후에 enum 으로 변경해야 함.
    // 1-제작사 2-유통사 3-가창자
    @Column
    private MemberType type;

    // 정산 대상 id
    @Column
    private Long memberId;

    // 정산일
    @Column
    private LocalDateTime settleDate;

    // 정산금
    @Column
    private Double fee;

    // 생성 날짜
    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    // 업데이트 날짜
    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Settlement(Contract contract, MemberType type, Long memberId, LocalDateTime settleDate, Double fee) {
        this.contract = contract;
        this.type = type;
        this.memberId = memberId;
        this.settleDate = settleDate;
        this.fee = fee;
    }

}
