package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.dto.UpdateRatingRequest;
import com.umc.movie.service.MemberService;
import com.umc.movie.service.RatingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final MemberService memberService;

    // 평점 등록
    @PostMapping("/{memberId}/ratings")
    public ResponseEntity<ApiResponse<Long>> createRating(
            @PathVariable Long memberId,
            @Valid @RequestBody CreateRatingRequest request
    ) {
        Long ratingId = ratingService.create(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(ratingId));
    }

    // 평점 목록 조회
    @GetMapping("/{memberId}/ratings")
    public ApiResponse<List<RatingResponse>> getRatings(
            @PathVariable Long memberId
    ) {
        return ApiResponse.ok(memberService.getRatings(memberId));
    }

    // 평점 수정
    @PatchMapping("/{memberId}/ratings/{ratingId}")
    public ApiResponse<Void> updateRating(
            @PathVariable Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request
    ) {
        ratingService.update(ratingId, memberId, request);
        return ApiResponse.ok(null);
    }

    // 평점 삭제
    @DeleteMapping("/{memberId}/ratings/{ratingId}")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Long memberId,
            @PathVariable Long ratingId
    ) {
        ratingService.delete(ratingId, memberId);
        return ResponseEntity.noContent().build();
    }
}
