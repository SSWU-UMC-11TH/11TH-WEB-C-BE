package com.umc.movie.service;

import com.umc.movie.domain.Member;
import com.umc.movie.domain.Rating;
import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.UpdateRatingRequest;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;

import com.umc.movie.exception.ApiException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;

    // 평점 등록
    @Transactional
    public Long create(Long memberId, CreateRatingRequest request) {

        // 회원 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "MEMBER404_1",
                        "해당 회원을 찾을 수 없습니다."
                ));

        // 중복 평점 확인
        if (ratingRepository.existsByMember_MemberIdAndMovieId(
                memberId, request.movieId())) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "RATING409_1",
                    "이미 해당 영화에 평점을 등록했습니다."
            );
        }

        // 평점 생성
        Rating rating = Rating.builder()
                .member(member)
                .movieId(request.movieId())
                .score(request.score())
                .comment(request.comment())
                .build();

        Rating savedRating = ratingRepository.save(rating);

        return savedRating.getRatingId();
    }

    // 평점 수정
    @Transactional
    public void update(
            Long ratingId,
            Long memberId,
            UpdateRatingRequest request
    ) {
        Rating rating = findOwnedRating(ratingId, memberId);

        rating.update(request.score(), request.comment());
    }

    // 평점 삭제
    @Transactional
    public void delete(Long ratingId, Long memberId) {
        Rating rating = findOwnedRating(ratingId, memberId);

        ratingRepository.delete(rating);
    }

    // 평점 조회 및 작성자 확인
    private Rating findOwnedRating(Long ratingId, Long memberId) {

        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "RATING404_1",
                        "존재하지 않는 평점입니다."
                ));

        // 작성자와 로그인한 회원이 같은지 확인
        if (!rating.getMember().getMemberId().equals(memberId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "RATING403_1",
                    "본인이 작성한 평점만 수정하거나 삭제할 수 있습니다."
            );
        }

        return rating;
    }
}
