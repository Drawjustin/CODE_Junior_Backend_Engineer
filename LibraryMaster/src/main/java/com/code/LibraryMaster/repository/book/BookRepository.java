package com.code.LibraryMaster.repository.book;

import com.code.LibraryMaster.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    boolean existsByAuthorId(Long authorId);
    boolean existsByIsbn(Long isbn);
    boolean existsByIsbnAndIdNot(Long isbn, Long id);
}
