package com.umc.first_spring.controller;

import com.umc.first_spring.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // POST http://localhost:8080/rentals
    @PostMapping
    public String createRental(@Valid @RequestBody RentalCreateRequest request) {
        Long userId = request.userId();
        Long bookId = request.bookId();
        return "도서 대여 기록이 정상적으로 생성되었습니다!";
    }
}
