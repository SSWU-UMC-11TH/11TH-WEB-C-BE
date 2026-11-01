package com.umc.study.repository;

import com.umc.study.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 도서 전체 조회 + 최신순 정렬
    @EntityGraph(attributePaths = "category")
    List<Book> findAllByOrderByBookIdDesc();

    // 특정 카테고리에 속한 도서 조회
    @EntityGraph(attributePaths = "category")
    List<Book> findByCategory_CategoryId(Long categoryId);
}