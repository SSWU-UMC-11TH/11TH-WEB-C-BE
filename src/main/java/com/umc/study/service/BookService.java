
package com.umc.study.service;

import com.umc.study.dto.book.BookResponse;
import com.umc.study.dto.book.CreateBookRequest;
import com.umc.study.entity.Book;
import com.umc.study.entity.Category;
import com.umc.study.repository.BookRepository;
import com.umc.study.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    // 1. 도서 전체 조회 (최신순)
    @Transactional(readOnly = true)
    public List<BookResponse> getBooks() {

        return bookRepository.findAllByOrderByBookIdDesc()
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    // 2. 신규 도서 등록
    @Transactional
    public BookResponse createBook(CreateBookRequest request) {

        // 카테고리 존재 여부 확인
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "존재하지 않는 카테고리입니다."
                        )
                );

        // Book 객체 생성
        Book book = new Book(
                category,
                request.title(),
                request.description()
        );

        // DB에 저장
        Book savedBook = bookRepository.save(book);

        // 응답 DTO로 변환
        return BookResponse.from(savedBook);
    }

    // 3. 카테고리별 도서 조회 (기존 기능 유지)
    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByCategory(Long categoryId) {

        return bookRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(BookResponse::from)
                .toList();
    }
}
