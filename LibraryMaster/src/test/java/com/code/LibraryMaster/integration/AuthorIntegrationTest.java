package com.code.LibraryMaster.integration;

import com.code.LibraryMaster.dto.author.AuthorCreateRequest;
import com.code.LibraryMaster.dto.author.AuthorResponse;
import com.code.LibraryMaster.dto.author.AuthorUpdateRequest;
import com.code.LibraryMaster.entity.Author;
import com.code.LibraryMaster.entity.Book;
import com.code.LibraryMaster.exception.BusinessException;
import com.code.LibraryMaster.exception.ErrorCodeCustom;
import com.code.LibraryMaster.repository.author.AuthorRepository;
import com.code.LibraryMaster.repository.book.BookRepository;
import com.code.LibraryMaster.service.AuthorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthorIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private AuthorCreateRequest createRequest;
    private AuthorUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        authorRepository.deleteAll();
        bookRepository.deleteAll();

        createRequest = AuthorCreateRequest.builder()
                .name("홍길동")
                .email("hong@example.com")
                .build();

        updateRequest = AuthorUpdateRequest.builder()
                .name("홍길동(수정)")
                .email("hong.updated@example.com")
                .build();
    }

    @Test
    @DisplayName("저자 등록 및 조회 통합 테스트")
    void registerAndFindAuthor() {
        // 저자 등록
        AuthorResponse createdAuthor = authorService.registerAuthor(createRequest);
        // 저장된 ID로 저자 조회
        AuthorResponse foundAuthor = authorService.findAuthorById(createdAuthor.getId());
        
        // 검증
        assertThat(foundAuthor).isNotNull();
        assertThat(createdAuthor.getName()).isEqualTo(foundAuthor.getName());
        assertThat(createdAuthor.getEmail()).isEqualTo(foundAuthor.getEmail());
    }

    @Test
    @DisplayName("저자 수정 통합 테스트")
    void updateAuthor() {
        // 저자 등록
        AuthorResponse createdAuthor = authorService.registerAuthor(createRequest);
        System.out.println(createdAuthor.getId());
        // 저자 수정
        AuthorResponse updatedAuthor = authorService.modifyAuthorInfo(createdAuthor.getId(), updateRequest);
        System.out.println(updatedAuthor.getId());
        // 저장된 ID로 저자 조회
        AuthorResponse foundAuthor = authorService.findAuthorById(updatedAuthor.getId());
        System.out.println(foundAuthor.getId());
        // 검증
        assertThat(foundAuthor.getName()).isEqualTo(updatedAuthor.getName());
        assertThat(foundAuthor.getEmail()).isEqualTo(updatedAuthor.getEmail());

    }

    @Test
    @DisplayName("저자 삭제 통합 테스트")
    void deleteAuthor() {
        // 저자 등록
        AuthorResponse createdAuthor = authorService.registerAuthor(createRequest);

        // 저자 삭제
        authorService.removeAuthor(createdAuthor.getId());

        assertFalse(authorRepository.existsById(createdAuthor.getId()));
    }

    @Test
    @DisplayName("저자 삭제 실패 - 연관된 도서 존재")
    void deleteAuthorWithBooks() {
        // 저자 등록
        AuthorResponse createdAuthor = authorService.registerAuthor(createRequest);
        
        // 저자와 연관된 도서 생성
        Author author = authorRepository.findById(createdAuthor.getId()).orElseThrow(() -> new BusinessException(ErrorCodeCustom.AUTHOR_NOT_FOUND));
        Book book = Book.builder()
                .title("테스트 도서")
                .isbn(1234567890L)
                .author(author)
                .build();

        bookRepository.save(book);
        
        assertThrows(BusinessException.class, () -> {
            authorService.removeAuthor(createdAuthor.getId());
        });

        assertTrue(authorRepository.existsById(createdAuthor.getId()));
    }

    @Test
    @DisplayName("이메일 중복 검사 통합 테스트")
    void duplicateEmailCheck() {
        // 저자 등록
        authorService.registerAuthor(createRequest);
        
        // 동일한 이메일로 다른 저자 등록 시도
        AuthorCreateRequest duplicateRequest =
                AuthorCreateRequest.builder()
                                .name("김철수")
                                .email("hong@example.com")
                                .build();
        // 예외 발생해야 함
        assertThrows(BusinessException.class, () -> {
            authorService.registerAuthor(duplicateRequest);
        });
    }

    @Test
    @DisplayName("모든 저자 목록 조회 통합 테스트")
    void findAllAuthors() {
        // 여러 저자 등록
        authorService.registerAuthor(createRequest);
        
        AuthorCreateRequest request2 = AuthorCreateRequest.builder()
                .name("김철수")
                .email("kim@example.com")
                .build();

        authorService.registerAuthor(request2);
        
        // 모든 저자 조회
        List<AuthorResponse> authors = authorService.findAllAuthors();

        assertEquals(2, authors.size());
        List<String> authorNames = authors.stream().map(AuthorResponse::getName).toList();
        assertTrue(authorNames.contains("홍길동"));
        assertTrue(authorNames.contains("김철수"));
    }
}