package com.code.LibraryMaster.service;

import com.code.LibraryMaster.dto.author.AuthorCreateRequest;
import com.code.LibraryMaster.dto.author.AuthorResponse;
import com.code.LibraryMaster.dto.author.AuthorUpdateRequest;
import com.code.LibraryMaster.entity.Author;
import com.code.LibraryMaster.repository.author.AuthorRepository;
import com.code.LibraryMaster.repository.book.BookRepository;
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

    @Transactional
    public AuthorResponse registerAuthor(AuthorCreateRequest authorCreateRequest) {
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
        return authorRepository.getAuthor(authorId);
    }

    @Transactional
    public List<AuthorResponse> findAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    @Transactional
    public AuthorResponse modifyAuthorInfo(Long authorId, AuthorUpdateRequest authorUpdateRequest) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: "));

        author.updateAuthor(authorUpdateRequest);

        return AuthorResponse.builder()
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }

    @Transactional
    public void removeAuthor(Long authorId) {
        authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: "));

        // 연관된 도서가 있는지 확인
        boolean hasBooks = bookRepository.existsByAuthorId(authorId);

        if (hasBooks) {
            throw new IllegalStateException("Cannot delete author with ID " + authorId +
                    " because there are books associated with this author. Remove the books first.");
        }


        authorRepository.deleteById(authorId);
    }
}
