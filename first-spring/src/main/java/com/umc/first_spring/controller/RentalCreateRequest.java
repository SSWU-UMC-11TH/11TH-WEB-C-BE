package com.umc.first_spring.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RentalCreateRequest(
        @NotNull @Positive Long userId,
        @NotNull @Positive Long bookId
) {}