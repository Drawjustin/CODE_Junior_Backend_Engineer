package com.code.LibraryMaster.dto.author;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorUpdateRequest {
    private String name;
    private String email;
}
