package com.umc.movie.controller;

import com.umc.movie.auth.AuthInterceptor;
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
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final MemberService memberService;

    // 평점 등록
    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse<Long>> createRating(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateRatingRequest request
    ) {
        Long ratingId = ratingService.create(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(ratingId));
    }

    // 평점 목록 조회 (공개 API)
    @GetMapping("/members/{memberId}/ratings")
    public ApiResponse<List<RatingResponse>> getRatings(
            @PathVariable Long memberId
    ) {
        return ApiResponse.ok(memberService.getRatings(memberId));
    }

    // 평점 수정
    @PatchMapping("/ratings/{ratingId}")
    public ApiResponse<Void> updateRating(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request
    ) {
        ratingService.update(ratingId, memberId, request);

        return ApiResponse.ok(null);
    }

    // 평점 삭제
    @DeleteMapping("/ratings/{ratingId}")
    public ApiResponse<Void> deleteRating(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId
    ) {
        ratingService.delete(ratingId, memberId);

        return ApiResponse.ok(null);
    }
}