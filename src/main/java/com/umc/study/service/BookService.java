package com.umc.study.service;

import com.umc.study.dto.BookResponse;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.entity.Book;
import com.umc.study.entity.Category;
import com.umc.study.exception.CategoryNotFoundException;
import com.umc.study.repository.BookRepository;
import com.umc.study.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

//    public List<Map<String, Object>> getAllBooks() {
//        return bookRepository.findAll();
//    }
//
//    public void createBook(Map<String, Object> body) {
//        bookRepository.save(body);
//    }
//
//    public List<Map<String, Object>> findCategory(Long categoryId)
//    { return bookRepository.findCategory(categoryId);}
//
//    public void createRental(Map<String, Object> body) {
//        bookRepository.createRental(body);
//    }
//
//    public void returnRental(Map<String, Object> body) {
//        bookRepository.returnRental(body);
//    }

    @Transactional(readOnly = true)
    public List<BookResponse> getBooks() {
        return bookRepository.findAllByOrderByBookIdDesc().stream()
                .map(BookResponse::from)
                .toList();
    }

//    @Transactional
//    public void createBook(CreateBookRequest request) {
//        Category category = categoryRepository.findById(request.categoryId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
//
//        Book book = new Book(category, request.title(), request.description());
//    }

    @Transactional
    public void createBook(CreateBookRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(CategoryNotFoundException::new);
        Book book = new Book(category, request.title(), request.description());

        bookRepository.save(book);
    }


}

//    @Transactional
//    public BookResponse createBook(CreateBookRequest request) {
//        Category category = categoryRepository.findById(request.categoryId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
//        Book book = new Book(category, request.title(), request.description());
//        return BookResponse.from(bookRepository.save(book));
//    }
