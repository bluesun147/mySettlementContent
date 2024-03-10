package com.haechan.mysettlement.ost.service;

import com.haechan.mysettlement.domain.ost.service.OstService;
import com.haechan.mysettlement.domain.drama.entity.Drama;
import com.haechan.mysettlement.domain.drama.repository.DramaRepository;
import com.haechan.mysettlement.domain.ost.dto.OstRegisterDto;
import com.haechan.mysettlement.domain.ost.entity.Ost;
import com.haechan.mysettlement.domain.ost.repository.OstRepository;
import com.haechan.mysettlement.domain.producer.entity.Producer;
import com.haechan.mysettlement.domain.producer.repository.ProducerRepository;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import com.haechan.mysettlement.domain.singer.repository.SingerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OstServiceTest {

    @Mock
    private OstRepository ostRepository;

    @Mock
    private DramaRepository dramaRepository;

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private SingerRepository singerRepository;

    // @Mock으로 만든 가짜 객체들 주입
    @InjectMocks
    private OstService ostService;


    @DisplayName("ost 등록 성공")
    @Test
    void register_Ost_success() {
        // given
        String ostTitle = "ostTitle";

        // 드라마
        Drama savedDrama = Drama.builder()
                .title("dramaName")
                .build();

        ReflectionTestUtils.setField(savedDrama, "id", 1L);

        // 제작사
        Producer savedProducer = Producer.builder()
                .name("producerName")
                .build();

        ReflectionTestUtils.setField(savedProducer, "id", 1L);

        // 가창자
        Singer savedSinger = Singer.builder()
                .name("singerName")
                .build();

        ReflectionTestUtils.setField(savedSinger, "id", 1L);

        OstRegisterDto ostRegisterDto = new OstRegisterDto(savedDrama.getId(), savedProducer.getId(), savedSinger.getId(), ostTitle);

        // mocking
        when(dramaRepository.findById(ostRegisterDto.getDramaId())).thenReturn(Optional.of(savedDrama));
        when(producerRepository.findById(ostRegisterDto.getProducerId())).thenReturn(Optional.of(savedProducer));
        when(singerRepository.findById(ostRegisterDto.getSingerId())).thenReturn(Optional.of(savedSinger));

        // when
        ostService.register(ostRegisterDto);

        // then
        verify(ostRepository).save(any(Ost.class));
    }

    @DisplayName("ost 등록 실패")
    @Test
    void register_Ost_fail() {
        // given
        String ostTitle = "ostTitle";
        Long invalidId = 99L;

        OstRegisterDto ostRegisterDto = new OstRegisterDto(invalidId, invalidId, invalidId, ostTitle);

        // mocking
        when(dramaRepository.findById(ostRegisterDto.getDramaId())).thenReturn(Optional.empty());

        // then
        assertThrows(Exception.class, () -> ostService.register(ostRegisterDto));
    }
}