package com.code.LibraryMaster.util;

import com.code.LibraryMaster.dto.book.BookCreateRequest;
import com.code.LibraryMaster.dto.book.BookUpdateRequest;
import com.code.LibraryMaster.exception.BusinessException;
import com.code.LibraryMaster.exception.ErrorCodeCustom;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookValidator {


    public void validateBookCreateRequest(BookCreateRequest request) {
        // 제목 검증
        if (!StringUtils.hasText(request.getTitle())) {
            throw new BusinessException(ErrorCodeCustom.BOOK_TITLE_REQUIRED);
        }

        // ISBN 검증
        if (request.getIsbn() == null) {
            throw new BusinessException(ErrorCodeCustom.BOOK_ISBN_REQUIRED);
        } else if (!isValidIsbn10(request.getIsbn())) {
            throw new BusinessException(ErrorCodeCustom.INVALID_ISBN_FORMAT);
        }

        // 저자 ID 검증
        if (request.getAuthor() == null) {
            throw new BusinessException(ErrorCodeCustom.BOOK_AUTHOR_REQUIRED);
        }

        // 모든 검증을 통과하면 예외 없이 리턴
    }

    public void validateBookUpdateRequest(BookUpdateRequest request) {
        // 제목 검증
        if (!StringUtils.hasText(request.getTitle())) {
            throw new BusinessException(ErrorCodeCustom.BOOK_TITLE_REQUIRED);
        }

        // ISBN 검증
        if (request.getIsbn() == null) {
            throw new BusinessException(ErrorCodeCustom.BOOK_ISBN_REQUIRED);
        } else if (!isValidIsbn10(request.getIsbn())) {
            throw new BusinessException(ErrorCodeCustom.INVALID_ISBN_FORMAT);
        }

        // 저자 ID 검증
        if (request.getAuthor() == null) {
            throw new BusinessException(ErrorCodeCustom.BOOK_AUTHOR_REQUIRED);
        }

        // 모든 검증을 통과하면 예외 없이 리턴
    }
    public boolean isValidIsbn10(Long isbn) {
        if (isbn == null) return false;

        // 10자리 숫자인지 확인 (1,000,000,000 ~ 9,999,999,999)
        if (isbn < 1_000_000_000L || isbn > 9_999_999_999L) return false;

        // 첫 두 자리 추출
        long firstTwoDigits = isbn / 100_000_000L;

        // 마지막 자리가 0인지 확인
        return isbn % 10 == 0;
    }

}
