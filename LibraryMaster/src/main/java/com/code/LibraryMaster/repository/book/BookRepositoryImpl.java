package com.code.LibraryMaster.repository.book;

import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.dto.book.BookSearchCondition;
import com.code.LibraryMaster.entity.Book;
import com.code.LibraryMaster.entity.QBook;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Page<BookResponse> getAllBooks(BookSearchCondition bookSearchCondition, Pageable pageable) {
        List<BookResponse> bookResponseList = queryFactory.select(Projections.constructor(BookResponse.class,
                        book.id,
                        book.title,
                        book.description,
                        book.isbn,
                        book.publicationDate,
                        book.author.id))
                .from(book)
                .innerJoin(book.author)
                .orderBy(getOrderByPublicationDate(bookSearchCondition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(book.count())
                .from(book)  
                .innerJoin(book.author);

        return PageableExecutionUtils.getPage(bookResponseList ,pageable, countQuery::fetchOne);

    }

    @Override
    public Optional<BookResponse> getBook(Long bookId) {
        BookResponse bookResponse = queryFactory.select(Projections.constructor(BookResponse.class,
                        book.id,
                        book.title,
                        book.description,
                        book.isbn,
                        book.publicationDate,
                        book.author.id))
                .from(book)
                .innerJoin(book.author)
                .where(book.id.eq(bookId))
                .fetchOne();

        return Optional.ofNullable(bookResponse);
    }
    private OrderSpecifier<?> getOrderByPublicationDate(BookSearchCondition bookSearchCondition) {
        return bookSearchCondition.getLatestFirst() == null || bookSearchCondition.getLatestFirst()
                ? book.publicationDate.desc()  // 최신순
                : book.publicationDate.asc();  // 오래된순
    }
}
