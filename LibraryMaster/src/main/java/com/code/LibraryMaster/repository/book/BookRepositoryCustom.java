package com.code.LibraryMaster.repository.book;

import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepositoryCustom {
    void createBook(Book book);
    List<BookResponse> getAllBooks();
    Optional<BookResponse> getBook(Long bookId);
}
