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
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
