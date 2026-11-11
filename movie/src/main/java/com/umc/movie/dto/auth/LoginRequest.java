
package com.umc.movie.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
