package com.code.LibraryMaster.repository.book;

import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.dto.book.BookSearchCondition;
import com.code.LibraryMaster.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepositoryCustom {
    void createBook(Book book);
    Page<BookResponse> getAllBooks(BookSearchCondition bookSearchCondition, Pageable pageable);
    Optional<BookResponse> getBook(Long bookId);
}
