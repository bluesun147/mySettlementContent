package com.haechan.mysettlement.domain.ost.entity;

import com.haechan.mysettlement.domain.drama.entity.Drama;
import com.haechan.mysettlement.domain.producer.entity.Producer;
import com.haechan.mysettlement.domain.singer.entity.Singer;
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

    // 제작사
    @ManyToOne
    @JoinColumn(name = "producerId")
    private Producer producer;

    // 가창자
    @ManyToOne
    @JoinColumn(name = "singerId")
    private Singer singer;

    // ost 제목
    @Column
    private String title;

    @Builder
    public Ost(Drama drama, Producer producer, Singer singer, String title) {
        this.drama = drama;
        this.producer = producer;
        this.singer = singer;
        this.title = title;
    }

}
