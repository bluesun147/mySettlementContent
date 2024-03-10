package com.haechan.mysettlement.singer.service;

import com.haechan.mysettlement.domain.singer.dto.SingerDto;
import com.haechan.mysettlement.domain.singer.entity.Singer;
import com.haechan.mysettlement.domain.singer.repository.SingerRepository;
import com.haechan.mysettlement.domain.singer.service.SingerService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// 이거랑 똑같은 방식 테스트 : https://galid1.tistory.com/772
// https://dncjf64.tistory.com/314
// Spring에서 Database Test 해보기 : https://velog.io/@minnseong/spring-db-test
// Service Layer 테스트하기 : https://galid1.tistory.com/772

// 망나니 개발자 TDD
// https://mangkyu.tistory.com/143
// https://mangkyu.tistory.com/182

// @SpringBootTest를 사용하지 않았기 때문에 IOC Container 생성 x (스프링에 의존 x)
// 필요한 서비스 객체만 실제로 생성되기 때문에 빠름
@ExtendWith(MockitoExtension.class)
class SingerServiceTest {

    // @Mock으로 만든 가짜 객체 자동으로 주입
    // 가짜 리포지토리 객체 주입
    @InjectMocks
    private SingerService singerService;

    // 가짜 객체 만들어 반환
    @Mock
    private SingerRepository singerRepository;

    @DisplayName("가창자 등록")
    @Test
    public void singer_register_test() {
        // given (준비)
        // 어떠한 데이터가 준비되었을 때
        SingerDto singerDto = createSingerDto();
        Singer singer = createSingerEntity(singerDto);

        Long fakeSingerId = 1L;
        // singer 객체의 id 필드 값을 fakeSingerId로 설정
        // 테스트 위해 객체의 상태를 특정값으로 지정
        // https://dncjf64.tistory.com/314#recentEntries
        // builder가 생성자에 붙어있기 때문에 private 값인 id 직접 할당
        // @Builder 상단에 붙이면 IDENTITY전략을 설정한 기본키 값 할당 가능하기 때문에 안전 x
        ReflectionTestUtils.setField(singer, "id", fakeSingerId);

        // mocking
        // 어떤 인자가 전달되더라도 특정한 값 반환하도록 설정
        given(singerRepository.save(any()))
                .willReturn(singer);
        // findById() 호출될 때 특정한 Singer 객체 반환
        given(singerRepository.findById(fakeSingerId))
                .willReturn(Optional.ofNullable(singer));

        // when (실행)
        // 어떠한 함수를 실행하면
        Long newSingerId = singerService.register(singerDto);

        // then (검증)
        // 어떠한 결과가 나와야 한다
        Singer findSinger = singerRepository.findById(newSingerId).get();

        // singer -
        // findSinger -
        assertEquals(singer.getId(), findSinger.getId(), "fail message");
        assertEquals(singer.getName(), findSinger.getName());

    }

    @Test
    public void register_성공() {
        // given
        SingerDto singerDto = createSingerDto();
        Singer savedSinger = createSingerEntity(singerDto);

        Long fakeSingerId = 1L;
        ReflectionTestUtils.setField(savedSinger, "id", fakeSingerId);


        // mocking
        // stub - 메소드의 결과 미리 지정
        // save에 어떤 값이 Singer 타입의 어떤 객체가 들어가도 savedSinger 리턴
        // when() : SingerRepository.save() 메서드가 호출될 때 어떤 행동을 해야 하는지 정의
        when(singerRepository.save(any(Singer.class))).thenReturn(savedSinger);

        // when
        Long registeredSingerId = singerService.register(singerDto);

        // then
        // mocking된 메서드가 실제 호출되었는지 검증
        verify(singerRepository).save(any(Singer.class));
        assertEquals(savedSinger.getId(), registeredSingerId);
    }

    private Singer createSingerEntity(SingerDto singerDto) {
        return Singer.builder()
                .name(singerDto.getName())
                .build();
    }

    private SingerDto createSingerDto() {
        SingerDto singerDto = new SingerDto();
        singerDto.setName("tom");
        return singerDto;
    }

}