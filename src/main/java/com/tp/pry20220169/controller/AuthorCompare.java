package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.service.AuthorService;
import com.tp.pry20220169.resource.AuthorResource;
import com.tp.pry20220169.resource.SaveAuthorResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Compare", description = "Compare API")
@RestController
@RequestMapping("/api/authors/compare")
@CrossOrigin
public class AuthorCompare {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<AuthorResource> compareAuthorsById(@Valid @RequestBody IdListWrapper idList, Pageable pageable){
        Page<Author> authorPage = authorService.getAllAuthorsByIdList(idList.getIds(), pageable);
        List<AuthorResource> resources = authorPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @PostMapping("")
    public Page<AuthorResource> compareAuthorsByIdPost(@Valid @RequestBody IdListWrapper idList, Pageable pageable){
        Page<Author> authorPage = authorService.getAllAuthorsByIdList(idList.getIds(), pageable);
        List<AuthorResource> resources = authorPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    private Author convertToEntity(SaveAuthorResource resource) { return mapper.map(resource, Author.class); }
    private AuthorResource convertToResource(Author entity) { return mapper.map(entity, AuthorResource.class); }

    @Data
    public static class IdListWrapper {
        List<Long> ids;
    }
}
