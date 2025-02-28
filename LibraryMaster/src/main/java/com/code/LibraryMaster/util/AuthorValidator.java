package com.code.LibraryMaster.util;

import com.code.LibraryMaster.dto.author.AuthorCreateRequest;
import com.code.LibraryMaster.dto.author.AuthorUpdateRequest;
import com.code.LibraryMaster.exception.BusinessException;
import com.code.LibraryMaster.exception.ErrorCodeCustom;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorValidator {
    public void validateAuthorCreateRequest(AuthorCreateRequest request) {
        // 이름 검증
        if (!StringUtils.hasText(request.getName())) {
            throw new BusinessException(ErrorCodeCustom.AUTHOR_NAME_REQUIRED);
        }

        // 이메일 검증
        if (!StringUtils.hasText(request.getEmail())) {
            throw new BusinessException(ErrorCodeCustom.AUTHOR_EMAIL_REQUIRED);
        } else if (!isValidEmail(request.getEmail())) {
            throw new BusinessException(ErrorCodeCustom.INVALID_EMAIL_FORMAT);
        }
    }

    public void validateAuthorUpdateRequest(AuthorUpdateRequest request) {
        // 이름 검증
        if (!StringUtils.hasText(request.getName())) {
            throw new BusinessException(ErrorCodeCustom.AUTHOR_NAME_REQUIRED);
        }

        // 이메일 검증
        if (!StringUtils.hasText(request.getEmail())) {
            throw new BusinessException(ErrorCodeCustom.AUTHOR_EMAIL_REQUIRED);
        } else if (!isValidEmail(request.getEmail())) {
            throw new BusinessException(ErrorCodeCustom.INVALID_EMAIL_FORMAT);
        }
    }
    private boolean isValidEmail(String email) {
        /**
         * 이메일 주소가 유효한지 검사합니다.
         * 이메일이 null이 아니면서 다음 조건을 만족하는지 확인합니다:
         * 1. 이메일 아이디 부분(@앞): 영문자, 숫자, 하이픈, 언더스코어, 점으로 구성
         * 2. @ 기호가 있음
         * 3. 도메인 부분(@뒤): 최소 하나 이상의 하위 도메인이 있고,
         *    각 도메인은 영문자, 숫자, 하이픈, 언더스코어로 구성되며 점으로 구분됨
         * 4. 최상위 도메인이 2~4글자의 영문자, 숫자, 하이픈, 언더스코어로 구성됨
         */
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
