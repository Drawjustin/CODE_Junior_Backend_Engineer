package com.code.LibraryMaster.repository.author;

import com.code.LibraryMaster.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long>, AuthorRepositoryCustom{

}
