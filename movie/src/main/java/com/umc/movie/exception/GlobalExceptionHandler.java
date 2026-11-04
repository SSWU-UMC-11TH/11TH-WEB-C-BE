package com.umc.movie.exception;

import com.umc.movie.dto.ApiResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // DTO 유효성 검사 실패: 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값이 올바르지 않습니다.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("COMMON4001", message));
    }

    // DB 제약조건 위반: 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityException(
            DataIntegrityViolationException e
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(
                        "COMMON4091",
                        "이미 사용 중인 정보입니다."
                ));
    }

    // 중복 이메일·닉네임 등 비즈니스 예외: 409
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure("COMMON4092", e.getMessage()));
    }

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatusException(
            ResponseStatusException e
    ) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());

        return ResponseEntity
                .status(status)
                .body(ApiResponse.failure(
                        "COMMON" + status.value(),
                        e.getReason() != null ? e.getReason() : "요청 처리에 실패했습니다."
                ));
    }
}
