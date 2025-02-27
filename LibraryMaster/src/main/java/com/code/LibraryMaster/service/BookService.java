package com.code.LibraryMaster.service;

import com.code.LibraryMaster.dto.book.BookCreateRequest;
import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.dto.book.BookSearchCondition;
import com.code.LibraryMaster.dto.book.BookUpdateRequest;
import com.code.LibraryMaster.entity.Author;
import com.code.LibraryMaster.entity.Book;
import com.code.LibraryMaster.exception.BusinessException;
import com.code.LibraryMaster.exception.ErrorCode;
import com.code.LibraryMaster.exception.ErrorCodeCustom;
import com.code.LibraryMaster.repository.author.AuthorRepository;
import com.code.LibraryMaster.repository.book.BookRepository;
import com.code.LibraryMaster.util.BookValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookValidator bookValidator;
    @Transactional
    public BookResponse registerBook(BookCreateRequest bookCreateRequest) {

        bookValidator.validateBookCreateRequest(bookCreateRequest);

        if (bookRepository.existsByIsbn(bookCreateRequest.getIsbn())) {
            throw new BusinessException(ErrorCodeCustom.DUPLICATE_ISBN);
        }

        Author author = authorRepository.findById(bookCreateRequest.getAuthor())
                .orElseThrow(() -> new BusinessException(ErrorCodeCustom.AUTHOR_NOT_FOUND));

        Book newBook = Book.builder()
                .title(bookCreateRequest.getTitle())
                .isbn(bookCreateRequest.getIsbn())
                .description(bookCreateRequest.getDescription())
                .publicationDate(bookCreateRequest.getPublicationDate())
                .author(author)
                .build();

        bookRepository.createBook(newBook);

        return BookResponse.builder()
                .id(newBook.getId())
                .title(newBook.getTitle())
                .isbn(newBook.getIsbn())
                .description(newBook.getDescription())
                .publicationDate(newBook.getPublicationDate())
                .author(newBook.getAuthor().getId())
                .build();
    }

    @Transactional
    public BookResponse findBookById(Long bookId) {
        return bookRepository.getBook(bookId).orElseThrow(() -> new BusinessException(ErrorCodeCustom.BOOK_NOT_FOUND));
    }

    @Transactional
    public Page<BookResponse> findAllBooks(BookSearchCondition bookSearchCondition, Pageable pageable) {
        return bookRepository.getAllBooks(bookSearchCondition, pageable);
    }

    @Transactional
    public BookResponse modifyBookInfo(Long bookId, BookUpdateRequest bookUpdateRequest) {

        bookValidator.validateBookUpdateRequest(bookUpdateRequest);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCodeCustom.BOOK_NOT_FOUND));

        if (bookRepository.existsByIsbnAndIdNot(bookUpdateRequest.getIsbn(), bookId)) {
            throw new BusinessException(ErrorCodeCustom.DUPLICATE_ISBN);
        }

        Author author = authorRepository.findById(bookUpdateRequest.getAuthor())
                .orElseThrow(() -> new BusinessException(ErrorCodeCustom.AUTHOR_NOT_FOUND));

        book.updateBook(bookUpdateRequest, author);

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .publicationDate(book.getPublicationDate())
                .author(book.getAuthor().getId()) // Author 객체에서 ID를 가져옴
                .build();
    }

    @Transactional
    public void removeBook(Long bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCodeCustom.BOOK_NOT_FOUND));

        bookRepository.deleteById(bookId);
    }
}
