package com.umc.movie.dto;

public record ApiResponse<T> (
        String message,
        T data
) {
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(message, data);
    }
}
