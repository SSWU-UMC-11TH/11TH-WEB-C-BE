package com.umc.study.controller;

import com.umc.study.dto.BookResponse;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponse> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping
    public ResponseEntity<Void> createBook(@Valid @RequestBody CreateBookRequest request) {
        bookService.createBook(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @GetMapping
//    public List<Map<String, Object>> getBooks() {
//        return bookService.getAllBooks();
//    }

//    @PostMapping
//    public String createBook(@RequestBody Map<String, Object> body) {
//        bookService.createBook(body);
//        return "도서 등록이 완료되었습니다!";
//    }
//
//    @GetMapping("/category/{categoryId}")
//    public List<Map<String, Object>> findCategory(@PathVariable Long categoryId) {
//        return bookService.findCategory(categoryId);
    }




