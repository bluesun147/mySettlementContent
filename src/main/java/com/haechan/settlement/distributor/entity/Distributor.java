package com.haechan.settlement.distributor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 유통사

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Distributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Double percent;

    @Builder
    Distributor(String name, Double percent) {
        this.name = name;
        this.percent = percent;
    }
}
