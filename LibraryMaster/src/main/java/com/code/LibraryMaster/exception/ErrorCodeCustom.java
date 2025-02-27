package com.code.LibraryMaster.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCodeCustom implements ErrorCode{
    AUTHOR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 저자를 찾을 수 없습니다."),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 도서를 찾을 수 없습니다."),

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),
    REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "필수 항목이 누락되었습니다."),

    AUTHOR_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "저자 이름은 필수 항목입니다."),
    AUTHOR_EMAIL_REQUIRED(HttpStatus.BAD_REQUEST, "저자 이메일은 필수 항목입니다."),
    BOOK_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "도서 제목은 필수 항목입니다."),
    BOOK_ISBN_REQUIRED(HttpStatus.BAD_REQUEST, "ISBN은 필수 항목입니다."),
    BOOK_AUTHOR_REQUIRED(HttpStatus.BAD_REQUEST, "도서의 저자 정보는 필수 항목입니다."),

    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    INVALID_ISBN_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 ISBN-10 형식입니다. ISBN은 10자리 숫자이며, 첫 두 자리는 10~90 사이, 마지막 자리는 0이어야 합니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일 주소입니다."),
    DUPLICATE_ISBN(HttpStatus.CONFLICT, "이미 등록된 ISBN입니다."),
    AUTHOR_HAS_BOOKS(HttpStatus.CONFLICT, "이 저자와 연관된 도서가 있어 삭제할 수 없습니다. 먼저 도서를 제거해 주세요."),

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 작업 중 오류가 발생했습니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
