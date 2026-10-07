package com.umc.study.controller;

import com.umc.study.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final BookService bookService;

    @PostMapping
    public String createRental(@RequestBody Map<String, Object> body) {
        bookService.createRental(body);
        return "신규 도서 대여 기록이 생성되었습니다!";
    }

    @PatchMapping("/{rentalId}/return")
    public String returnRental(@RequestBody Map<String, Object> body) {
        bookService.returnRental(body);
        return "도서 반납 처리가 완료되었습니다!";
    }

}
