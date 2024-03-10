package com.haechan.content.domain.ost.service;

import com.haechan.content.domain.ost.dto.OstRegisterDto;
import com.haechan.content.domain.drama.entity.Drama;
import com.haechan.content.domain.drama.repository.DramaRepository;
import com.haechan.content.global.feign.ProducerFeignClient;
import com.haechan.content.global.feign.ProducerFeignResponse;
import com.haechan.content.domain.ost.entity.Ost;
import com.haechan.content.domain.ost.repository.OstRepository;
import com.haechan.content.global.feign.SingerFeignClient;
import com.haechan.content.global.feign.SingerFeignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OstService {

    private final OstRepository ostRepository;
    private final DramaRepository dramaRepository;
    private final ProducerFeignClient producerFeignClient;
    private final SingerFeignClient singerFeignClient;

    public void register(OstRegisterDto ostRegisterDto) {

        Long dramaId = ostRegisterDto.getDramaId();
        // findById Optional orElseThrow
        // https://dev-coco.tistory.com/178
        Drama drama = dramaRepository.findById(dramaId).orElseThrow();

        Long producerId = ostRegisterDto.getProducerId();

        Long singerId = ostRegisterDto.getSingerId();

        ProducerFeignResponse producerFeignResponse = producerFeignClient.findProducerById(producerId);
        log.info("producerFeignResponse.getProducerId() = {}", producerFeignResponse.getProducerId());
        log.info("producerFeignResponse.getName() = {}", producerFeignResponse.getName());

        SingerFeignResponse singerFeignResponse = singerFeignClient.findSingerById(singerId);
        log.info("singerFeignResponse.getSingerId() = {}", singerFeignResponse.getSingerId());
        log.info("singerFeignResponse.getName() = {}", singerFeignResponse.getName());

        Ost ost = Ost.builder()
                .drama(drama)
                .producerId(producerFeignResponse.getProducerId())
                .singerId(singerFeignResponse.getSingerId())
                .title(ostRegisterDto.getTitle())
                .build();

        ostRepository.save(ost);
    }
}
