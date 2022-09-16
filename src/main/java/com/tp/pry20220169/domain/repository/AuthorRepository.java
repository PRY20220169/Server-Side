package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findByLastName(String lastName, Pageable pageable);
    Page<Author> findByFirstName(String firstName, Pageable pageable);
    Page<Author> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);
}
