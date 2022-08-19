package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.repository.AuthorRepository;
import com.tp.pry20220169.domain.service.AuthorService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Page<Author> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
    }

    @Override
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(Long authorId, Author authorDetails) {
        return authorRepository.findById(authorId).map(author -> {
            author.setFirstName(authorDetails.getFirstName());
            author.setLastName(authorDetails.getLastName());
            author.setAddress(authorDetails.getAddress());
            author.setEmail(authorDetails.getEmail());
            author.setOrganizations(authorDetails.getOrganizations());
            author.setMetrics(authorDetails.getMetrics());
            return authorRepository.save(author);
        }).orElseThrow(() -> new ResourceNotFoundException("Authors", "Id", authorId));
    }

    @Override
    public ResponseEntity<?> deleteAuthor(Long authorId) {
        return authorRepository.findById(authorId).map(author -> {
            authorRepository.delete(author);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
    }
}
