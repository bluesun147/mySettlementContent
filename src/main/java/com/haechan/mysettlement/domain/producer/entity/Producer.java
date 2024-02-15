package com.haechan.mysettlement.domain.producer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 제작사

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Builder
    Producer(String name) {
        this.name = name;
    }
}
