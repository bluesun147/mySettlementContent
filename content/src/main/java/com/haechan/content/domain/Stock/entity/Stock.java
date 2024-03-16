package com.haechan.content.domain.stock.entity;

import com.haechan.content.domain.ost.entity.Ost;
import com.haechan.content.global.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Stock extends BaseTime {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ostId")
    private Ost ost;

    @Column
    private Long quantity;

    @Version
    private Long version;

    // quantity 증가
    public void increase(final Long quantity) {
        this.quantity = this.quantity + quantity;
    }

    // quantity 감소
    public void decrease(final Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new RuntimeException("재고 부족");
        }
        this.quantity = this.quantity - quantity;
    }

    @Builder
    public Stock(Long id, Ost ost, Long quantity) {
        this.id = id;
        this.ost = ost;
        this.quantity = quantity;
    }
}
