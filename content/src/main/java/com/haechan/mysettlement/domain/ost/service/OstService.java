package com.haechan.mysettlement.domain.ost.service;

import com.haechan.mysettlement.domain.ost.dto.OstRegisterDto;
import com.haechan.mysettlement.domain.drama.entity.Drama;
import com.haechan.mysettlement.domain.drama.repository.DramaRepository;
import com.haechan.mysettlement.global.feign.ProducerFeignClient;
import com.haechan.mysettlement.global.feign.ProducerFeignResponse;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import com.haechan.mysettlement.domain.ost.repository.OstRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OstService {

    private final OstRepository ostRepository;
    private final DramaRepository dramaRepository;
    private final ProducerFeignClient producerFeignClient;

    public void register(OstRegisterDto ostRegisterDto) {

        Long dramaId = ostRegisterDto.getDramaId();
        // findById Optional orElseThrow
        // https://dev-coco.tistory.com/178
        Drama drama = dramaRepository.findById(dramaId).orElseThrow();

        Long producerId = ostRegisterDto.getProducerId();

        Long singerId = ostRegisterDto.getSingerId();

        ProducerFeignResponse response = producerFeignClient.findProducerById(producerId);

        Ost ost = Ost.builder()
                .drama(drama)
                .producerId(response.getProducerId())
                .singerId(singerId)
                .title(ostRegisterDto.getTitle())
                .build();

        ostRepository.save(ost);
    }
}
