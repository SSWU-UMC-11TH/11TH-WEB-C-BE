
package com.umc.movie.auth;

import com.umc.movie.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    private SecretKey key;

    // JWT 서명에 사용할 비밀키 초기화
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Access Token 생성
    public String createAccessToken(Long memberId) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + accessExpiration * 1000
        );

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    // JWT 검증 및 Claims 추출
    public Claims getClaims(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {

            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_4",
                    "토큰이 만료되었습니다. 다시 로그인해주세요."
            );

        } catch (JwtException | IllegalArgumentException e) {

            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_3",
                    "유효하지 않은 토큰입니다."
            );
        }
    }

    // JWT에서 회원 ID 추출
    public Long getMemberId(String token) {

        Claims claims = getClaims(token);

        try {
            return Long.parseLong(claims.getSubject());

        } catch (NumberFormatException e) {

            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_3",
                    "유효하지 않은 토큰입니다."
            );
        }
    }
}
