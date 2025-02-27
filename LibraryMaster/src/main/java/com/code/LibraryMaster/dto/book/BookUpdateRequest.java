package com.code.LibraryMaster.dto.book;

import com.code.LibraryMaster.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookUpdateRequest {
    private String title;
    private String description;
    private Long isbn;
    private LocalDateTime publicationDate;
    private Long author;
}
