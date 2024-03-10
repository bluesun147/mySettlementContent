package com.haechan.mysettlement.domain.singer.entity;

import jakarta.persistence.*;
import lombok.*;

// 가창자

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Singer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Builder
    Singer(String name) {
        this.name = name;
    }
}
