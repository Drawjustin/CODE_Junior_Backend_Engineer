package com.code.LibraryMaster.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BookSearchCondition {
    private Boolean latestFirst = true; // true: 최신순, false: 오래된순
}
