package com.code.LibraryMaster.entity;

import com.code.LibraryMaster.dto.book.BookUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(name = "publication_date", nullable = true)
    private LocalDateTime publicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    public void updateBook(BookUpdateRequest bookUpdateRequest, Author author){
        this.title = bookUpdateRequest.getTitle();
        this.description = bookUpdateRequest.getDescription();
        this.isbn = bookUpdateRequest.getIsbn();
        this.publicationDate = bookUpdateRequest.getPublicationDate();
        this.author = author;
    }
}
