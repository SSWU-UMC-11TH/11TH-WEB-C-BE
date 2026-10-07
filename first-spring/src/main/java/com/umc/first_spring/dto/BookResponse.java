package com.umc.first_spring.dto;

import com.umc.first_spring.entity.Book;

public record BookResponse(
        Long bookId,
        String title,
        String description,
        String categoryName,
        Boolean isAvailable
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getBookId(),
                book.getTitle(),
                book.getDescription(),
                book.getCategory().getName(), // 엔티티의 카테고리 이름 메서드에 맞춰 호출
                book.getIsAvailable()
        );
    }
}