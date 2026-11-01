
package com.umc.study.controller;

import com.umc.study.dto.book.BookResponse;
import com.umc.study.dto.book.CreateBookRequest;
import com.umc.study.service.BookService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // GET /books : 도서 전체 조회
    @GetMapping
    public List<BookResponse> getBooks() {
        return bookService.getBooks();
    }

    // POST /books : 신규 도서 등록
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(
            @Valid @RequestBody CreateBookRequest request
    ) {
        return bookService.createBook(request);
    }

    // GET /books/category/{categoryId} : 카테고리별 조회
    @GetMapping("/category/{categoryId}")
    public List<BookResponse> getBooksByCategory(
            @PathVariable Long categoryId
    ) {
        return bookService.getBooksByCategory(categoryId);
    }
}
