package com.haechan.mysettlement.domain.Stock;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Stock {
    @Id
    private Long id;
    private Long productId;
    private Long quantity;
    @Version
    private Long version;

    public Stock(final Long id, final Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    // quantity 감소
    public void decrease(final Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new RuntimeException("재고 부족");
        }
        this.quantity = this.quantity - quantity;
    }
}
