package com.haechan.content.domain.ost.entity;

import com.haechan.content.domain.drama.entity.Drama;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
public class Ost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 드라마
    @ManyToOne
    @JoinColumn(name = "dramaId")
    private Drama drama;

    // 간접 참조로 변경
    // 제작사
    @Column
    private Long producerId;

    // 가창자
    @Column
    private Long singerId;

    // ost 제목
    @Column
    private String title;

    @Builder
    public Ost(Drama drama, Long producerId, Long singerId, String title) {
        this.drama = drama;
        this.producerId = producerId;
        this.singerId = singerId;
        this.title = title;
    }

}
