package com.umc.study.controller;

import com.umc.study.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<String> createRental(@RequestBody Map<String, Object> requestData) {
        Long userId = Long.valueOf(requestData.get("userId").toString());
        Long bookId = Long.valueOf(requestData.get("bookId").toString());

        rentalService.createRental(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body("대여 기록 생성 성공!");
    }
}