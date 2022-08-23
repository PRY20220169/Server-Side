package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.service.ArticleService;
import com.tp.pry20220169.domain.service.AuthorService;
import com.tp.pry20220169.resource.ArticleResource;
import com.tp.pry20220169.resource.AuthorResource;
import com.tp.pry20220169.resource.SaveArticleResource;
import com.tp.pry20220169.resource.SaveAuthorResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Articles", description = "Articles API")
@RestController
@RequestMapping("/api/articles/{articleId}")
@CrossOrigin
public class ArticleAuthorsController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public Page<AuthorResource> getAllAuthorsByArticleId(
            @PathVariable(name = "articleId") Long articleId,
            Pageable pageable) {
        List<AuthorResource> authors = authorService.getAllAuthorsByArticleId(articleId, pageable)
                .getContent().stream().map(this::convertToResource)
                .collect(Collectors.toList());
        int authorsCount = authors.size();
        return new PageImpl<>(authors, pageable, authorsCount);
    }

    @PostMapping("/authors/{authorId}")
    public ArticleResource addArticleAuthor(
            @PathVariable(name = "articleId") Long articleId,
            @PathVariable(name = "authorId") Long authorId) {
        return convertToResource(articleService.addArticleAuthor(articleId, authorId));
    }

    @DeleteMapping("/authors/{authorId}")
    public ArticleResource removeArticleAuthor(
            @PathVariable(name = "articleId") Long articleId,
            @PathVariable(name = "authorId") Long authorId) {
        return convertToResource(articleService.removeArticleAuthor(articleId, authorId));
    }

    private Article convertToEntity(SaveArticleResource resource) {
        return mapper.map(resource, Article.class);
    }
    private ArticleResource convertToResource(Article entity) {
        return mapper.map(entity, ArticleResource.class);
    }

    private Author convertToEntity(SaveAuthorResource resource) {
        return mapper.map(resource, Author.class);
    }
    private AuthorResource convertToResource(Author entity) {
        return mapper.map(entity, AuthorResource.class);
    }
}
