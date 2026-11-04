package com.umc.movie.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRatingRequest(

        @NotNull(message = "평점은 필수입니다.")
        @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
        @Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
        Integer score,

        @Size(max = 500, message = "감상평은 500자 이하여야 합니다.")
        String comment

) {
}