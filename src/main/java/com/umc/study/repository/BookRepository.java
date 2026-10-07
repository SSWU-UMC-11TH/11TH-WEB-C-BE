package com.umc.study.repository;

import com.umc.study.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository
//@RequiredArgsConstructor
//public class BookRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public List<Map<String, Object>> findAll() {
//        String sql = "SELECT * FROM book";
//
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    public void save(Map<String, Object> body) {
//        String sql = "INSERT INTO book(category_id, title, description, is_available) VALUES (?,?,?,true)";
//
//        jdbcTemplate.update(sql, body.get("categoryId"),body.get("title"),body.get("description"));
//    }
//
//    public List<Map<String, Object>> findCategory(Long categoryId) {
//        String sql = "SELECT * FROM book WHERE category_id = ?";
//        return jdbcTemplate.queryForList(sql, categoryId);
//    }
//
//    public void createRental(Map<String, Object> body) {
//        String sql = "INSERT INTO rental(user_id, book_id, rented_at, due_at) VALUES (?,?,NOW(),DATE_ADD(NOW(), INTERVAL 7 DAY))";
//        jdbcTemplate.update(sql, body.get("userId"), body.get("bookId"));
//    }
//
//    public void returnRental(Map<String, Object> body) {
//        String sql = "UPDATE rental SET returned_at=now() WHERE rental_id=?";
//        jdbcTemplate.update(sql, body.get("rentalId"));
//    }
//
//
//}

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByOrderByBookIdDesc();
}
