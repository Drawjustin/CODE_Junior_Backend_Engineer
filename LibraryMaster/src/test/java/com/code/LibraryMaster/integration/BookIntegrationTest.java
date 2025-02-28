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
                .publicationDate(LocalDateTime.now().minusDays(1L))
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
    @DisplayName("도서 검색 조건 통합 테스트 (출판일 기준)")
    void searchBooksByCondition() {
        // 여러 도서 등록
        BookResponse firstBook = bookService.registerBook(bookCreateRequest);
        
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
                .publicationDate(LocalDateTime.now())
                .author(anotherAuthor.getId())
                .build();

        BookResponse secondBook = bookService.registerBook(anotherBookRequest);

        // 최신순 정렬 조건 (latestFirst = true)
        BookSearchCondition latestFirstCondition = BookSearchCondition.builder()
                .latestFirst(true)
                .build();

        // 오래된순 정렬 조건 (latestFirst = false)
        BookSearchCondition oldestFirstCondition = BookSearchCondition.builder()
                .latestFirst(false)
                .build();

        // 최신순 조회 결과 확인
        Page<BookResponse> latestFirstResults = bookService.findAllBooks(latestFirstCondition, PageRequest.of(0, 10));
        assertThat(latestFirstResults.getTotalElements()).isEqualTo(2);
        assertThat(latestFirstResults.getContent().get(0).getId()).isEqualTo(secondBook.getId()); // 최신 도서가 첫 번째로
        assertThat(latestFirstResults.getContent().get(1).getId()).isEqualTo(firstBook.getId()); // 오래된 도서가 두 번째로

        // 오래된순 조회 결과 확인
        Page<BookResponse> oldestFirstResults = bookService.findAllBooks(oldestFirstCondition, PageRequest.of(0, 10));
        assertThat(oldestFirstResults.getTotalElements()).isEqualTo(2);
        assertThat(oldestFirstResults.getContent().get(0).getId()).isEqualTo(firstBook.getId()); // 오래된 도서가 첫 번째로
        assertThat(oldestFirstResults.getContent().get(1).getId()).isEqualTo(secondBook.getId()); // 최신 도서가 두 번째로
    }
    @Test
    @DisplayName("제목이 빈 문자열인 도서 등록 실패 테스트")
    void registerBookWithEmptyTitle() {
        // 빈 제목으로 도서 생성 요청
        BookCreateRequest invalidRequest = BookCreateRequest.builder()
                .title("")
                .description("테스트 설명")
                .isbn(1234567890L)
                .author(createdAuthor.getId())
                .build();

        // 예외 발생 검증
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookService.registerBook(invalidRequest);
        });

        assertEquals(ErrorCodeCustom.BOOK_TITLE_REQUIRED, exception.getErrorCode());
    }
    @Test
    @DisplayName("ISBN 유효성 검증 테스트")
    void registerBookWithInvalidIsbn() {
        // 9자리 ISBN (10자리 미만)
        BookCreateRequest invalidRequest = BookCreateRequest.builder()
                .title("테스트 도서")
                .description("테스트 설명")
                .isbn(123456789L) // 9자리 ISBN
                .author(createdAuthor.getId())
                .build();

        // 첫 두자리 유효범위 벗어남 (10~90)
        BookCreateRequest invalidRequest2 = BookCreateRequest.builder()
                .title("테스트 도서")
                .description("테스트 설명")
                .isbn(9234567890L) // 첫 두자리가 92로 유효범위(10~90) 벗어남
                .author(createdAuthor.getId())
                .build();

        // 마지막 자리가 0으로 끝나지 않음
        BookCreateRequest invalidRequest3 = BookCreateRequest.builder()
                .title("테스트 도서")
                .description("설명")
                .isbn(1234567891L) // 마지막 자리가 유효하지 않은 체크 디지트
                .author(createdAuthor.getId())
                .build();


        // 예외 발생 검증
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookService.registerBook(invalidRequest);
        });

        // 예외 발생 검증
        BusinessException exception2 = assertThrows(BusinessException.class, () -> {
            bookService.registerBook(invalidRequest);
        });

        // 예외 발생 검증
        BusinessException exception3 = assertThrows(BusinessException.class, () -> {
            bookService.registerBook(invalidRequest);
        });


        assertEquals(ErrorCodeCustom.INVALID_ISBN_FORMAT, exception.getErrorCode());
        assertEquals(ErrorCodeCustom.INVALID_ISBN_FORMAT, exception2.getErrorCode());
        assertEquals(ErrorCodeCustom.INVALID_ISBN_FORMAT, exception3.getErrorCode());

    }

    @Test
    @DisplayName("페이지네이션 테스트")
    void paginationTest() {
        // 여러 도서 등록 (11개)
        for (int i = 0; i < 11; i++) {
            BookCreateRequest request = BookCreateRequest.builder()
                    .title("도서 " + i)
                    .description("설명 " + i)
                    .isbn(1234567890L + i * 10)
                    .author(createdAuthor.getId())
                    .build();
            bookService.registerBook(request);
        }

        // 첫 번째 페이지(10개 항목) 조회
        Page<BookResponse> firstPage = bookService.findAllBooks(BookSearchCondition.builder().build(), PageRequest.of(0, 10));

        // 두 번째 페이지(나머지 항목) 조회
        Page<BookResponse> secondPage = bookService.findAllBooks(BookSearchCondition.builder().build(), PageRequest.of(1, 10));

        // 검증
        assertThat(firstPage.getTotalElements()).isEqualTo(11);
        assertThat(firstPage.getContent().size()).isEqualTo(10);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(secondPage.getContent().size()).isEqualTo(1);
    }
}