package com.umc.movie.controller;

import com.umc.movie.dto.EmailCheckResponse;
import com.umc.movie.dto.NicknameCheckResponse;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.service.MemberService;
import com.umc.movie.dto.MemberResponse;
import com.umc.movie.dto.SignUpRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.umc.movie.dto.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        MemberResponse response = memberService.signUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping("/nickname/{nickname}")
    public NicknameCheckResponse checkNickname(
            @PathVariable String nickname
    ) {
        return memberService.checkNickname(nickname);
    }

    @GetMapping("/email/{email}")
    public EmailCheckResponse checkEmail(
            @PathVariable String email
    ) {
        return memberService.checkEmail(email);
    }

}