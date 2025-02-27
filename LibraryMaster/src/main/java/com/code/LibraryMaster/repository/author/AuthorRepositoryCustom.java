package com.code.LibraryMaster.repository.author;

import com.code.LibraryMaster.dto.author.AuthorResponse;
import com.code.LibraryMaster.entity.Author;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepositoryCustom {
    void createAuthor(Author author);
    List<AuthorResponse> getAllAuthors();
    Optional<AuthorResponse> getAuthor(Long authorId);
}
