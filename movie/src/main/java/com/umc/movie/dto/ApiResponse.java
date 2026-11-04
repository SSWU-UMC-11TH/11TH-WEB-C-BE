package com.umc.movie.dto;

public record ApiResponse<T>(
        boolean isSuccess,
        String code,
        String message,
        T result
) {

    // 생성 성공: HTTP 201
    public static <T> ApiResponse<T> created(T result) {
        return new ApiResponse<>(
                true,
                "COMMON201",
                "요청이 성공적으로 처리되었습니다.",
                result
        );
    }

    // 일반 성공: HTTP 200
    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>(
                true,
                "COMMON200",
                "요청이 성공적으로 처리되었습니다.",
                result
        );
    }

    // 실패
    public static <T> ApiResponse<T> failure(
            String code,
            String message
    ) {
        return new ApiResponse<>(
                false,
                code,
                message,
                null
        );
    }
}
