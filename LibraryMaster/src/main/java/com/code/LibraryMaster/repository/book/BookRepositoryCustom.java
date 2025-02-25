package com.code.LibraryMaster.repository.book;

import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepositoryCustom {
    void createBook(Book book);
    List<BookResponse> getAllBooks();
    BookResponse getBook(Long bookId);
}
