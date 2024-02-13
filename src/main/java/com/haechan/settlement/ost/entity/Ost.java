package com.haechan.settlement.ost.entity;

import com.haechan.settlement.drama.entity.Drama;
import com.haechan.settlement.producer.entity.Producer;
import com.haechan.settlement.singer.entity.Singer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
