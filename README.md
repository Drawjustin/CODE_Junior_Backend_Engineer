# CODE_Junior_Backend_Engineer
도서 관리 시스템 CRUD 과제
# LibraryMaster - 도서 관리 시스템

LibraryMaster는 도서와 저자 정보를 관리하는 간단한 RESTful API 시스템입니다. 이 시스템은 저자(Author)와 도서(Book) 간의 관계를 관리하며, 각 엔티티에 대한 CRUD 기능을 제공합니다.

## 기술 스택

- **언어:** Java 17
- **프레임워크:** Spring Boot 3
- **빌드 도구:** Gradle
- **데이터베이스:** H2 (인메모리)
- **ORM:** Spring Data JPA, QueryDSL
- **API 문서화:** Swagger (OpenAPI 3.0)

## 기능 요약

### 저자(Author) 관리
- 저자 등록, 조회, 수정, 삭제 기능
- 이메일 유효성 검사 및 중복 체크
- 저자 삭제 시 연관된 도서에 대한 처리 정책 적용

### 도서(Book) 관리
- 도서 등록, 조회, 수정, 삭제 기능
- ISBN 유효성 검사 및 중복 체크
- 도서와 저자 간의 연관 관계 관리

## 설치 및 실행 방법

### 사전 요구사항
- JDK 17 이상
- Gradle

### 로컬 실행 방법

1. 프로젝트 클론
   ```bash
   git clone https://github.com/Drawjustin/LibraryMaster.git
   cd LibraryMaster
   ```

2. 애플리케이션 빌드
   ```bash
   ./gradlew build
   ```

3. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

4. 서버 접속
   - 기본 URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 콘솔: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:librarydb`)

## API 엔드포인트

### 저자(Author) API

| 메소드 | 엔드포인트 | 설명 |
|--------|------------|------|
| POST   | /authors   | 새로운 저자 등록 |
| GET    | /authors   | 모든 저자 목록 조회 |
| GET    | /authors/{id} | 특정 저자 상세 정보 조회 |
| PUT    | /authors/{id} | 특정 저자 정보 수정 |
| DELETE | /authors/{id} | 특정 저자 삭제 (연관된 도서가 없는 경우에만 가능) |

### 도서(Book) API

| 메소드 | 엔드포인트 | 설명 |
|--------|------------|------|
| POST   | /books     | 새로운 도서 등록 |
| GET    | /books     | 모든 도서 목록 조회 |
| GET    | /books/{id} | 특정 도서 상세 정보 조회 |
| PUT    | /books/{id} | 특정 도서 정보 수정 |
| DELETE | /books/{id} | 특정 도서 삭제 |

## API 사용 예시

### 저자(Author) API 사용법

#### 1. 저자 등록
```http
POST /authors
Content-Type: application/json

{
  "name": "홍길동",
  "email": "hong@example.com"
}
```
**응답 예시:**
```json
{
  "name": "홍길동",
  "email": "hong@example.com"
}
```

#### 2. 모든 저자 목록 조회
```http
GET /authors
```
**응답 예시:**
```json
[
  {
    "name": "홍길동",
    "email": "hong@example.com"
  },
  {
    "name": "김철수",
    "email": "kim@example.com"
  }
]
```

#### 3. 특정 저자 상세 정보 조회
```http
GET /authors/1
```
**응답 예시:**
```json
{
  "name": "홍길동",
  "email": "hong@example.com"
}
```

#### 4. 저자 정보 수정
```http
PUT /authors/1
Content-Type: application/json

{
  "name": "홍길동(수정)",
  "email": "hong.updated@example.com"
}
```
**응답 예시:**
```json
{
  "name": "홍길동(수정)",
  "email": "hong.updated@example.com"
}
```

#### 5. 저자 삭제
```http
DELETE /authors/1
```
**응답:** HTTP 204 No Content

**⚠️ 주의사항:**
- 저자를 삭제하기 전에 해당 저자와 연관된 모든 도서를 먼저 삭제해야 합니다.
- 연관된 도서가 있는 경우, 삭제 요청은 실패하고 적절한 에러 메시지가 반환됩니다.

### 도서(Book) API 사용법

#### 1. 도서 등록
```http
POST /books
Content-Type: application/json

{
  "title": "예제 도서",
  "description": "도서에 대한 설명",
  "isbn": "1034567890",
  "publicationDate": "2025-01-01",
  "authorId": 1
}
```
**응답 예시:**
```json
{
  "title": "예제 도서",
  "description": "도서에 대한 설명",
  "isbn": "1034567890",
  "publicationDate": "2025-01-01",
  "authorId": 1,
  "authorName": "홍길동"
}
```

#### 2. 모든 도서 목록 조회
```http
GET /books
```
**응답 예시:**
```json
[
  {
    "title": "예제 도서",
    "description": "도서에 대한 설명",
    "isbn": "1034567890",
    "publicationDate": "2025-01-01",
    "authorId": 1,
    "authorName": "홍길동"
  },
  {
    "title": "다른 예제 도서",
    "description": "다른 도서에 대한 설명",
    "isbn": "2034567890",
    "publicationDate": "2025-02-01",
    "authorId": 2,
    "authorName": "김철수"
  }
]
```

#### 3. 특정 도서 상세 정보 조회
```http
GET /books/1
```
**응답 예시:**
```json
{
  "title": "예제 도서",
  "description": "도서에 대한 설명",
  "isbn": "1034567890",
  "publicationDate": "2025-01-01",
  "authorId": 1,
  "authorName": "홍길동"
}
```

#### 4. 도서 정보 수정
```http
PUT /books/1
Content-Type: application/json

{
  "title": "수정된 도서 제목",
  "description": "수정된 도서 설명",
  "isbn": "1034567890",
  "publicationDate": "2025-03-01",
  "authorId": 1
}
```
**응답 예시:**
```json
{
  "title": "수정된 도서 제목",
  "description": "수정된 도서 설명",
  "isbn": "1034567890",
  "publicationDate": "2025-03-01",
  "authorId": 1,
  "authorName": "홍길동"
}
```

#### 5. 도서 삭제
```http
DELETE /books/1
```
**응답:** HTTP 204 No Content

## 유효성 검사 및 비즈니스 로직

### 고유성 체크
- ISBN은 도서마다 유일해야 합니다 (ISBN-10 규칙 적용).
- 이메일은 저자마다 유일해야 합니다.

### ISBN-10 유효성 검사 규칙
- 10자리 숫자로 구성
- 국가/언어 식별 번호 (첫 2자리): 10~90 사이의 숫자
- 출판사 식별 번호 (다음 3-6자리)
- 책 식별 번호 (다음 7-9자리)
- 체크 디지트 (마지막 자리): 0 사용

### 필수 필드
- 저자: 이름, 이메일
- 도서: 제목, ISBN, 저자 ID

### 에러 처리
- 400 Bad Request: 잘못된 입력 데이터
- 404 Not Found: 존재하지 않는 리소스 접근
- 409 Conflict: 중복된 ISBN 또는 이메일
- 500 Internal Server Error: 서버 내부 오류

## 주의사항 및 비즈니스 정책

### 저자 삭제 정책
- 저자를 삭제하기 위해서는 해당 저자와 연관된 모든 도서가 먼저 삭제되어야 합니다.
- 연관된 도서가 있는 경우 삭제가 불가능하며, 다음과 같은 에러 메시지가 반환됩니다:
  ```json
  {
    "status": 409,
    "message": "이 저자와 연관된 도서가 있어 삭제할 수 없습니다. 먼저 도서를 제거해 주세요."
  }
  ```

### ISBN 유효성 검사
- 모든 ISBN은 ISBN-10 규칙을 따라야 합니다.
- 잘못된 형식의 ISBN이 제공되면 다음과 같은 에러가 발생합니다:
  ```json
  {
    "status": 400,
    "message": "유효하지 않은 ISBN-10 형식입니다. ISBN은 10자리 숫자이며, 첫 두 자리는 10~90 사이, 마지막 자리는 0이어야 합니다."
  }
  ```

- 이미 등록된 ISBN로 책을 생성하려고 할 경우, 다음과 같은 에러가 발생합니다:
  ```json
  {
    "status": 409,
    "message": "이미 등록된 ISBN입니다."
  }
  ```

### 이메일 유효성 검사
- 이메일은 표준 이메일 형식을 따라야 합니다.
- 이미 등록된 이메일로 저자를 생성하려고 할 경우, 다음과 같은 에러가 발생합니다:
  ```json
  {
    "status": 409,
    "message": "이미 사용 중인 이메일입니다."
  }
  ```

### 필수 필드 누락
- 필수 필드(저자 이름, 이메일, 도서 제목, ISBN 등)가 누락된 경우 적절한 에러 메시지가 반환됩니다.
  ```json
  {
    "status": 400,
    "message": "####은 필수 항목입니다."
  }
  ```

### 존재하지 않는 저자 ID
- 존재하지 않는 저자 ID로 도서를 생성하려고 할 경우, 다음과 같은 에러가 발생합니다:
  ```json
  {
    "status": 404,
    "message": "해당 ID의 저자를 찾을 수 없습니다."
  }
  ```
### 존재하지 않는 책 ID
- 존재하지 않는 책 ID로 도서를 생성하려고 할 경우, 다음과 같은 에러가 발생합니다:
  ```json
  {
    "status": 404,
    "message": "해당 ID의 도서를 찾을 수 없습니다."
  }
  ```

## Swagger 문서
Swagger UI를 통해 모든 API 엔드포인트를 확인하고 테스트할 수 있습니다.
- 접속 URL: `http://localhost:8080/swagger-ui.html`

## 프로젝트 구조
```
com.code.LibraryMaster/
├── config/
│   ├── QueryDslConfig.java  - QueryDsl 설정
├── controller/
│   ├── AuthorController.java  - 저자 관련 API 엔드포인트 처리
│   └── BookController.java    - 도서 관련 API 엔드포인트 처리
├── dto/
│   ├── author/
│   │   ├── AuthorCreateRequest.java  - 저자 생성 요청 DTO
│   │   ├── AuthorResponse.java       - 저자 응답 DTO
│   │   └── AuthorUpdateRequest.java  - 저자 수정 요청 DTO
│   └── book/
│       ├── BookCreateRequest.java    - 도서 생성 요청 DTO
│       ├── BookResponse.java         - 도서 응답 DTO
│       └── BookUpdateRequest.java    - 도서 수정 요청 DTO
├── entity/
│   ├── Author.java  - 저자 엔티티
│   └── Book.java    - 도서 엔티티
├── exception/
│   ├── BusinessException.java  - 비즈니스 예외 클래스
│   ├── ErrorCode.java  - 에러 코드 인터페이스
│   └── ErrorCodeCustom.java  - 사용자 정의 에러 코드 구현
├── repository/
│   ├── author/
│   │   ├── AuthorRepository.java  - 저자 레포지토리 인터페이스
│   │   ├── AuthorRepositoryCustom.java  - 저자 커스텀 레포지토리 인터페이스
│   │   └── AuthorRepositoryImpl.java  - 저자 레포지토리 구현체
│   └── book/
│       ├── BookRepository.java  - 도서 레포지토리 인터페이스
│       ├── BookRepositoryCustom.java  - 도서 커스텀 레포지토리 인터페이스
│       └── BookRepositoryImpl.java  - 도서 레포지토리 구현체
├── service/
│   ├── AuthorService.java  - 저자 관련 비즈니스 로직
│   └── BookService.java    - 도서 관련 비즈니스 로직
├── util/
│   ├── AuthorValidator.java  - 저자 유효성 검증 유틸리티
│   └── BookValidator.java    - 도서 유효성 검증 유틸리티
└── BookSmartApplication.java  - 애플리케이션 메인 클래스
```

## 테스트
주요 기능에 대한 단위 테스트와 통합 테스트가 구현되어 있습니다. 테스트는 다음 명령어로 실행할 수 있습니다.
```bash
./gradlew test
```
