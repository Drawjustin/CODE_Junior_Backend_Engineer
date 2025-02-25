package com.code.LibraryMaster.entity;

import com.code.LibraryMaster.dto.author.AuthorUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    public void updateAuthor(AuthorUpdateRequest authorUpdateRequest){
        this.name = authorUpdateRequest.getName();
        this.email = authorUpdateRequest.getEmail();
    }
}
