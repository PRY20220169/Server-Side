package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findTopByOrderByIdDesc();
}
