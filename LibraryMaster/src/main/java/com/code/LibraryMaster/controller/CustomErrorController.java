package com.code.LibraryMaster.controller;

import com.code.LibraryMaster.exception.ErrorCodeCustom;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ErrorCodeCustom> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(ErrorCodeCustom.INVALID_PATH, ErrorCodeCustom.INVALID_PATH.getHttpStatus());
    }
}