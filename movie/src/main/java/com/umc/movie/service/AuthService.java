
package com.umc.movie.service;

import com.umc.movie.auth.JwtUtil;
import com.umc.movie.domain.Member;
import com.umc.movie.dto.auth.LoginRequest;
import com.umc.movie.dto.auth.LoginResponse;
import com.umc.movie.exception.ApiException;
import com.umc.movie.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    public LoginResponse login(LoginRequest request) {

        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "AUTH401_1",
                        "이메일 또는 비밀번호가 올바르지 않습니다."
                ));

        // 비밀번호 검증
        if (!passwordEncoder.matches(
                request.password(),
                member.getPassword()
        )) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_1",
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        // JWT Access Token 생성
        String accessToken = jwtUtil.createAccessToken(
                member.getMemberId()
        );

        // 로그인 결과 반환
        return new LoginResponse(
                member.getNickname(),
                accessToken
        );
    }
}
