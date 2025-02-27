package com.code.LibraryMaster.integration;

import com.code.LibraryMaster.dto.author.AuthorCreateRequest;
import com.code.LibraryMaster.dto.author.AuthorResponse;
import com.code.LibraryMaster.dto.book.BookCreateRequest;
import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.dto.book.BookSearchCondition;
import com.code.LibraryMaster.dto.book.BookUpdateRequest;
import com.code.LibraryMaster.entity.Author;
import com.code.LibraryMaster.entity.Book;
import com.code.LibraryMaster.exception.BusinessException;
import com.code.LibraryMaster.exception.ErrorCodeCustom;
import com.code.LibraryMaster.repository.author.AuthorRepository;
import com.code.LibraryMaster.repository.book.BookRepository;
import com.code.LibraryMaster.service.AuthorService;
import com.code.LibraryMaster.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private AuthorCreateRequest authorCreateRequest;
    private AuthorResponse createdAuthor;
    private BookCreateRequest bookCreateRequest;
    private BookUpdateRequest bookUpdateRequest;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        // 저자 생성
        authorCreateRequest = AuthorCreateRequest.builder()
                .name("홍길동")
                .email("hong@example.com")
                .build();
        createdAuthor = authorService.registerAuthor(authorCreateRequest);

        // 도서 생성 요청 데이터 준비
        bookCreateRequest = BookCreateRequest.builder()
                .title("테스트 도서")
                .description("테스트 도서 설명")
                .isbn(1234567890L)
                .publicationDate(LocalDateTime.now())
                .author(createdAuthor.getId())
                .build();
        // 도서 생성 요청 데이터 준비
        bookCreateRequest = BookCreateRequest.builder()
                .title("테스트 도서")
                .description("테스트 도서 설명")
                .isbn(1234567890L)
                .publicationDate(LocalDateTime.now())
                .author(createdAuthor.getId())
                .build();

        // 도서 업데이트 요청 데이터 준비
        bookUpdateRequest = BookUpdateRequest.builder()
                .title("수정된 도서 제목")
                .description("수정된 도서 설명")
                .isbn(1234567890L)
                .publicationDate(LocalDateTime.now().plusDays(1))
                .author(createdAuthor.getId())
                .build();
    }

    @Test
    @DisplayName("도서 등록 및 조회 통합 테스트")
    void registerAndFindBook() {
        // 도서 등록
        BookResponse createdBook = bookService.registerBook(bookCreateRequest);
        
        // 저장된 ID로 도서 조회
        BookResponse foundBook = bookService.findBookById(createdBook.getId());
        
        // 검증
        assertThat(foundBook).isNotNull();
        assertThat(createdBook.getTitle()).isEqualTo(foundBook.getTitle());
        assertThat(createdBook.getIsbn()).isEqualTo(foundBook.getIsbn());
        assertThat(createdBook.getAuthor()).isEqualTo(foundBook.getAuthor());
    }

    @Test
    @DisplayName("도서 수정 통합 테스트")
    void updateBook() {
        // 도서 등록
        BookResponse createdBook = bookService.registerBook(bookCreateRequest);
        
        // 도서 수정
        BookResponse updatedBook = bookService.modifyBookInfo(createdBook.getId(), bookUpdateRequest);
        
        // 저장된 ID로 도서 조회
        BookResponse foundBook = bookService.findBookById(updatedBook.getId());
        
        // 검증
        assertThat(foundBook.getTitle()).isEqualTo(bookUpdateRequest.getTitle());
        assertThat(foundBook.getDescription()).isEqualTo(bookUpdateRequest.getDescription());
        assertThat(foundBook.getIsbn()).isEqualTo(bookUpdateRequest.getIsbn());
    }

    @Test
    @DisplayName("도서 삭제 통합 테스트")
    void deleteBook() {
        // 도서 등록
        BookResponse createdBook = bookService.registerBook(bookCreateRequest);

        // 도서 삭제
        bookService.removeBook(createdBook.getId());

        // 검증
        assertFalse(bookRepository.existsById(createdBook.getId()));
    }

    @Test
    @DisplayName("ISBN 중복 검사 통합 테스트")
    void duplicateIsbnCheck() {
        // 도서 등록
        bookService.registerBook(bookCreateRequest);
        
        // 동일한 ISBN으로 다른 도서 등록 시도
        BookCreateRequest duplicateRequest = BookCreateRequest.builder()
                .title("중복 ISBN 도서")
                .description("다른 설명")
                .isbn(9788956746425L) // 같은 ISBN
                .author(createdAuthor.getId())
                .build();
                
        // 예외 발생해야 함
        assertThrows(BusinessException.class, () -> {
            bookService.registerBook(duplicateRequest);
        });
    }

    @Test
    @DisplayName("존재하지 않는 저자로 도서 등록 시도 테스트")
    void registerBookWithNonExistentAuthor() {
        // 존재하지 않는 저자 ID로 도서 생성 요청
        BookCreateRequest invalidRequest = BookCreateRequest.builder()
                .title("저자 없는 도서")
                .description("설명")
                .isbn(1234567890L)
                .author(9999L) // 존재하지 않는 저자 ID
                .build();
                
        // 예외 발생해야 함
        assertThrows(BusinessException.class, () -> {
            bookService.registerBook(invalidRequest);
        });
    }

    @Test
    @DisplayName("도서 검색 조건 통합 테스트")
    void searchBooksByCondition() {
        // 여러 도서 등록
        bookService.registerBook(bookCreateRequest);
        
        // 다른 저자 생성
        AuthorCreateRequest anotherAuthorRequest = AuthorCreateRequest.builder()
                .name("김철수")
                .email("kim@example.com")
                .build();
        AuthorResponse anotherAuthor = authorService.registerAuthor(anotherAuthorRequest);
        
        // 다른 저자의 도서 생성
        BookCreateRequest anotherBookRequest = BookCreateRequest.builder()
                .title("다른 저자의 도서")
                .description("다른 설명")
                .isbn(1234567810L)
                .author(anotherAuthor.getId())
                .build();
        bookService.registerBook(anotherBookRequest);
        

        BookSearchCondition Condition = BookSearchCondition.builder()
                .latestFirst(true)
                .build();
        
        // 전체 도서 조회
        Page<BookResponse> allResults = bookService.findAllBooks(Condition, PageRequest.of(0, 10));
        assertThat(allResults.getTotalElements()).isEqualTo(2);
    }
}