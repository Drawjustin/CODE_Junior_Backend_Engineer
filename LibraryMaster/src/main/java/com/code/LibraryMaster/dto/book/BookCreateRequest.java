package com.code.LibraryMaster.dto.book;

import com.code.LibraryMaster.entity.Author;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCreateRequest {
    private String title;
    private String description;
    private Long isbn;
    private LocalDateTime publicationDate;
    private Long author;
}
