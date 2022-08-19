package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AuthorService {
    Page<Author> getAllAuthors(Pageable pageable);
    Author getAuthorById(Long authorId);
    Author createAuthor(Author author);
    Author updateAuthor(Long authorId, Author authorDetails);
    ResponseEntity<?> deleteAuthor(Long authorId);
}
