
package com.umc.movie.auth;

import com.umc.movie.exception.ApiException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    // 요청에서 회원 ID를 꺼낼 때 사용할 이름
    public static final String MEMBER_ID = "memberId";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        // CORS 사전 요청은 통과
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // Authorization 헤더 가져오기
        String authorization = request.getHeader(
                HttpHeaders.AUTHORIZATION
        );

        // 토큰이 없거나 Bearer 형식이 아닌 경우
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_2",
                    "로그인이 필요한 요청입니다."
            );
        }

        // Bearer 뒤의 실제 토큰 추출
        String token = authorization.substring(7);

        // 빈 토큰 검사
        if (token.isBlank()) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_2",
                    "로그인이 필요한 요청입니다."
            );
        }

        // JWT 검증 후 회원 ID 추출
        Long memberId = jwtUtil.getMemberId(token);

        // 요청 객체에 회원 ID 저장
        request.setAttribute(MEMBER_ID, memberId);

        // 다음 단계(Controller)로 진행
        return true;
    }
}
