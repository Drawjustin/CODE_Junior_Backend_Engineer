package com.code.LibraryMaster.controller;

import com.code.LibraryMaster.dto.book.BookCreateRequest;
import com.code.LibraryMaster.dto.book.BookResponse;
import com.code.LibraryMaster.dto.book.BookUpdateRequest;
import com.code.LibraryMaster.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    @PostMapping("")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookCreateRequest bookCreateRequest){
        return ResponseEntity.ok(bookService.registerBook(bookCreateRequest));
    }
    @GetMapping("")
    public ResponseEntity<List<BookResponse>> getAllBooks(){
        return ResponseEntity.ok(bookService.findAllBooks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable("id") Long bookId){
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long bookId, @RequestBody BookUpdateRequest bookUpdateRequest){
        return ResponseEntity.ok(bookService.modifyBookInfo(bookId, bookUpdateRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long bookId){
        bookService.removeBook(bookId);
        return ResponseEntity.noContent().build();
    }

}
