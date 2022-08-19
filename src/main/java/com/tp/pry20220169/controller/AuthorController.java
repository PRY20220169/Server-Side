package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.service.AuthorService;
import com.tp.pry20220169.resource.AuthorResource;
import com.tp.pry20220169.resource.SaveAuthorResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Authors", description = "Authors API")
@RestController
@RequestMapping("/api/authors")
@CrossOrigin
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<AuthorResource> getAllAuthors(Pageable pageable){
        Page<Author> authorPage = authorService.getAllAuthors(pageable);
        List<AuthorResource> resources = authorPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/{authorId}")
    public AuthorResource getAuthorById(@PathVariable(name = "authorId") Long authorId){
        return convertToResource(authorService.getAuthorById(authorId));
    }

    @PostMapping("")
    public AuthorResource createAuthor(@Valid @RequestBody SaveAuthorResource resource){
        Author author = convertToEntity(resource);
        return convertToResource(authorService.createAuthor(author));
    }

    @PutMapping("/{authorId}")
    public AuthorResource updateAuthor(@PathVariable(name = "authorId") Long authorId,
                                         @Valid @RequestBody SaveAuthorResource resource){
        Author author = convertToEntity(resource);
        return convertToResource(authorService.updateAuthor(authorId, author));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable(name = "authorId") Long authorId){
        return authorService.deleteAuthor(authorId);
    }

    private Author convertToEntity(SaveAuthorResource resource) { return mapper.map(resource, Author.class); }
    private AuthorResource convertToResource(Author entity) { return mapper.map(entity, AuthorResource.class); }
}
