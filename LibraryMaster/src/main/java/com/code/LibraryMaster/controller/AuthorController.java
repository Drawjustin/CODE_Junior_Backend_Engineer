package com.code.LibraryMaster.controller;

import com.code.LibraryMaster.dto.author.AuthorCreateRequest;
import com.code.LibraryMaster.dto.author.AuthorResponse;
import com.code.LibraryMaster.dto.author.AuthorUpdateRequest;
import com.code.LibraryMaster.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping("")
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody AuthorCreateRequest authorCreateRequest){
        return ResponseEntity.ok(authorService.registerAuthor(authorCreateRequest));
    }
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorResponse>> getAllAuthors(){
        return ResponseEntity.ok(authorService.findAllAuthors());
    }
    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable("id") Long authorId){
        return ResponseEntity.ok(authorService.findAuthorById(authorId));
    }
    @PutMapping("/authors/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable("id") Long authorId, @RequestBody AuthorUpdateRequest authorUpdateRequest){
        return ResponseEntity.ok(authorService.modifyAuthorInfo(authorId, authorUpdateRequest));
    }

    // TODO : Author를 삭제할 때, 반드시 해당 작가와 연관된 모든 도서를 먼저 삭제해야 한다. 그렇지 않을 시, 삭제 불가
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long authorId){
        authorService.removeAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

}
