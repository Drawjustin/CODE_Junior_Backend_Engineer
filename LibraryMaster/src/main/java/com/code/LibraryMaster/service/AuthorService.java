package com.code.LibraryMaster.service;

import com.code.LibraryMaster.dto.author.AuthorCreateRequest;
import com.code.LibraryMaster.dto.author.AuthorResponse;
import com.code.LibraryMaster.dto.author.AuthorUpdateRequest;
import com.code.LibraryMaster.entity.Author;
import com.code.LibraryMaster.exception.BusinessException;
import com.code.LibraryMaster.exception.ErrorCodeCustom;
import com.code.LibraryMaster.repository.author.AuthorRepository;
import com.code.LibraryMaster.repository.book.BookRepository;
import com.code.LibraryMaster.util.AuthorValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorValidator authorValidator;

    @Transactional
    public AuthorResponse registerAuthor(AuthorCreateRequest authorCreateRequest) {
        authorValidator.validateAuthorCreateRequest(authorCreateRequest);

        Author newAuthor = Author.builder()
                .name(authorCreateRequest.getName())
                .email(authorCreateRequest.getEmail())
                .build();

        authorRepository.createAuthor(newAuthor);

        return AuthorResponse.builder()
                .name(authorCreateRequest.getName())
                .email(authorCreateRequest.getEmail())
                .build();
    }

    @Transactional
    public AuthorResponse findAuthorById(Long authorId) {
        return authorRepository.getAuthor(authorId).orElseThrow(() -> new BusinessException(ErrorCodeCustom.AUTHOR_NOT_FOUND));
    }

    @Transactional
    public List<AuthorResponse> findAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    @Transactional
    public AuthorResponse modifyAuthorInfo(Long authorId, AuthorUpdateRequest authorUpdateRequest) {
        authorValidator.validateAuthorUpdateRequest(authorUpdateRequest);

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new BusinessException(ErrorCodeCustom.AUTHOR_NOT_FOUND));

        if (authorRepository.existsByEmailAndIdNot(authorUpdateRequest.getEmail(), authorId)) {
            throw new BusinessException(ErrorCodeCustom.DUPLICATE_EMAIL);
        }

        author.updateAuthor(authorUpdateRequest);

        return AuthorResponse.builder()
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }

    @Transactional
    public void removeAuthor(Long authorId) {
        authorRepository.findById(authorId)
                .orElseThrow(() -> new BusinessException(ErrorCodeCustom.AUTHOR_NOT_FOUND));

        // 연관된 도서가 있는지 확인
        if(bookRepository.existsByAuthorId(authorId)){
            throw new BusinessException(ErrorCodeCustom.AUTHOR_HAS_BOOKS);
        }

        authorRepository.deleteById(authorId);
    }
}
