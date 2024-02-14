package com.haechan.settlement.ost.service;

import com.haechan.settlement.drama.entity.Drama;
import com.haechan.settlement.drama.repository.DramaRepository;
import com.haechan.settlement.ost.dto.OstRegisterDto;
import com.haechan.settlement.ost.entity.Ost;
import com.haechan.settlement.ost.repository.OstRepository;
import com.haechan.settlement.producer.entity.Producer;
import com.haechan.settlement.producer.repository.ProducerRepository;
import com.haechan.settlement.singer.entity.Singer;
import com.haechan.settlement.singer.repository.SingerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OstService {

    private final OstRepository ostRepository;
    private final DramaRepository dramaRepository;
    private final ProducerRepository producerRepository;
    private final SingerRepository singerRepository;


    public void register(OstRegisterDto ostRegisterDto) {

        Long dramaId = ostRegisterDto.getDramaId();
        // findById Optional orElseThrow
        // https://dev-coco.tistory.com/178
        Drama drama = dramaRepository.findById(dramaId).orElseThrow();

        Long producerId = ostRegisterDto.getProducerId();
        Producer producer = producerRepository.findById(producerId).orElseThrow();

        Long singerId = ostRegisterDto.getSingerId();
        Singer singer = singerRepository.findById(singerId).orElseThrow();


        Ost ost = Ost.builder()
                .drama(drama)
                .producer(producer)
                .singer(singer)
                .title(ostRegisterDto.getTitle())
                .build();

        ostRepository.save(ost);
    }
}
