package com.code.LibraryMaster.repository.author;

import com.code.LibraryMaster.dto.author.AuthorResponse;
import com.code.LibraryMaster.entity.Author;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.code.LibraryMaster.entity.QAuthor.*;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryImpl implements AuthorRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void createAuthor(Author author) {
        em.persist(author);
    }

    @Override
    public List<AuthorResponse> getAllAuthors() {
        return queryFactory
                .select(Projections.constructor(AuthorResponse.class,
                        author.id,
                        author.name,
                        author.email))
                .from(author)
                .fetch();
    }

    @Override
    public Optional<AuthorResponse> getAuthor(Long authorId) {
        AuthorResponse authorResponse = queryFactory
                .select(Projections.constructor(AuthorResponse.class,
                        author.id,
                        author.name,
                        author.email))
                .from(author)
                .where(author.id.eq(authorId))
                .fetchOne();

        return Optional.ofNullable(authorResponse);
    }


}
