
package com.umc.movie.dto.auth;

public record LoginResponse(
        String nickname,
        String accessToken
) {
}

