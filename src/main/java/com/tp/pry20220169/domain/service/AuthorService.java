package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorService {
    Page<Author> getAllAuthors(Pageable pageable);
    Page<Author> getAllAuthorsByArticleId(Long articleId, Pageable pageable);
    Author getAuthorById(Long authorId);
    Page<Author> getAuthorsByFirstName(String firstName, Pageable pageable);
    Page<Author> getAuthorsByLastName(String lastName, Pageable pageable);
    Page<Author> getAuthorsByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);
    Author createAuthor(Author author);
    Author updateAuthor(Long authorId, Author authorDetails);
    ResponseEntity<?> deleteAuthor(Long authorId);
    Page<Author> getAllAuthorsByIdList(List<Long> ids, Pageable pageable);
}
