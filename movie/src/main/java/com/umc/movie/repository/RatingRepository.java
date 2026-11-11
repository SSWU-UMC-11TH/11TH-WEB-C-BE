package com.umc.movie.repository;

import com.umc.movie.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // 회원별 평점 목록 조회 (최신순)
    List<Rating> findByMember_MemberIdOrderByRatingIdDesc(Long memberId);

    // 회원이 해당 영화에 이미 평점을 등록했는지 확인
    boolean existsByMember_MemberIdAndMovieId(Long memberId, Long movieId);
}