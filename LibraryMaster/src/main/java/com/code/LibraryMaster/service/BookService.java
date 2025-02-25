package com.code.LibraryMaster.service;

import com.code.LibraryMaster.dto.author.AuthorUpdateRequest;
import com.code.LibraryMaster.dto.book.BookCreateRequest;
import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.dto.book.BookUpdateRequest;
import com.code.LibraryMaster.entity.Author;
import com.code.LibraryMaster.entity.Book;
import com.code.LibraryMaster.repository.author.AuthorRepository;
import com.code.LibraryMaster.repository.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    @Transactional
    public BookResponse registerBook(BookCreateRequest bookCreateRequest) {
        Author author = authorRepository.findById(bookCreateRequest.getAuthor())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: "));

        Book newBook = Book.builder()
                .title(bookCreateRequest.getTitle())
                .isbn(bookCreateRequest.getIsbn())
                .description(bookCreateRequest.getDescription())
                .publicationDate(bookCreateRequest.getPublicationDate())
                .author(author)
                .build();

        bookRepository.createBook(newBook);

        return BookResponse.builder()
                .title(newBook.getTitle())
                .isbn(newBook.getIsbn())
                .description(newBook.getDescription())
                .publicationDate(newBook.getPublicationDate())
                .author(newBook.getAuthor().getId())
                .build();
    }

    @Transactional
    public BookResponse findBookById(Long bookId) {
        return bookRepository.getBook(bookId);
    }

    @Transactional
    public List<BookResponse> findAllBooks() {
        return bookRepository.getAllBooks();
    }

    @Transactional
    public BookResponse modifyBookInfo(Long bookId, BookUpdateRequest bookUpdateRequest) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: "));

        Author author = authorRepository.findById(bookUpdateRequest.getAuthor())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: "));

        book.updateBook(bookUpdateRequest, author);

        return BookResponse.builder()
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
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: "));

        bookRepository.deleteById(bookId);
    }
}
