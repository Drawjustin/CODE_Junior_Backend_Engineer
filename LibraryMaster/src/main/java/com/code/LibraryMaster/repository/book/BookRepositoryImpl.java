package com.code.LibraryMaster.repository.book;

import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.entity.Book;
import com.code.LibraryMaster.entity.QBook;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.code.LibraryMaster.entity.QBook.*;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public void createBook(Book book) {
        em.persist(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return queryFactory.select(Projections.constructor(BookResponse.class,
                        book.title,
                        book.description,
                        book.isbn,
                        book.publicationDate,
                        book.author.id))
                .from(book)
                .innerJoin(book.author)
                .fetch();
    }

    @Override
    public BookResponse getBook(Long bookId) {
        return queryFactory.select(Projections.constructor(BookResponse.class,
                        book.title,
                        book.description,
                        book.isbn,
                        book.publicationDate,
                        book.author.id))
                .from(book)
                .innerJoin(book.author)
                .where(book.id.eq(bookId))
                .fetchOne();
    }
}
