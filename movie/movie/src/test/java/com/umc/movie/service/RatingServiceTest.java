// src/test/java/com/umc/movie/service/RatingServiceTest.java
package com.umc.movie.service;

import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.UpdateRatingRequest;
import com.umc.movie.entity.Member;
import com.umc.movie.entity.Rating;
import com.umc.movie.exception.ApiException;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    @DisplayName("같은 영화에 평점을 중복 등록하면 RATING409_1 예외를 던지고 저장하지 않는다")
    void createRatingWhenDuplicated() {
        // given
        Member member = Member.builder()
                .email("challenger@umc.com")
                .password("암호화된비밀번호")
                .nickname("영화조아")
                .build();

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(ratingRepository.existsByMember_MemberIdAndMovieId(1L, 550L)).willReturn(true);

        CreateRatingRequest request = new CreateRatingRequest(550L, 5, "재밌어요");

        // when & then
        assertThatThrownBy(() -> ratingService.create(1L, request))
                .isInstanceOf(ApiException.class)
                .hasMessage("이미 이 영화에 평점을 남기셨습니다.")
                .extracting("code")
                .isEqualTo("RATING409_1");

        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    @DisplayName("다른 사람의 평점을 수정하려고 하면 RATING403_1 예외를 던지고 저장하지 않는다")
    void updateRatingWhenNotOwner() {
        // given: 평점 작성자는 1번 회원인데, 2번 회원이 수정을 시도하는 상황
        Member owner = Member.builder()
                .email("owner@umc.com")
                .password("암호화된비밀번호")
                .nickname("작성자")
                .build();
        ReflectionTestUtils.setField(owner, "memberId", 1L);

        Rating rating = Rating.builder()
                .member(owner)
                .movieId(550L)
                .score(4)
                .comment("원래 한줄평")
                .build();

        given(ratingRepository.findById(10L)).willReturn(Optional.of(rating));

        UpdateRatingRequest request = new UpdateRatingRequest(1, "남의 평점 수정 시도");

        // when & then
        assertThatThrownBy(() -> ratingService.update(2L, 10L, request))
                .isInstanceOf(ApiException.class)
                .hasMessage("본인이 작성한 평점만 수정하거나 삭제할 수 있습니다.")
                .extracting("code")
                .isEqualTo("RATING403_1");

        verify(ratingRepository, never()).save(any(Rating.class));
    }
}