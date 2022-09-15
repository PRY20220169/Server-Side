package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.service.ArticleService;
import com.tp.pry20220169.resource.ArticleResource;
import com.tp.pry20220169.resource.SaveArticleResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Articles Search", description = "Articles Search API")
@RestController
@RequestMapping("/api/articles/search")
@CrossOrigin
public class ArticleSearchController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/keywords")
    public Page<ArticleResource> searchByKeywords(@Valid @RequestBody KeywordsWrapper keywords, Pageable pageable){
        Page<Article> articlePage = articleService.getAllArticlesByKeywords(keywords.getKeywords(), pageable);
        List<ArticleResource> resources = articlePage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/categories")
    public Page<ArticleResource> searchByCategories(@Valid @RequestBody CategoriesWrapper categories, Pageable pageable){
        Page<Article> articlePage = articleService.getAllArticlesByCategories(categories.getCategories(), pageable);
        List<ArticleResource> resources = articlePage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    private Article convertToEntity(SaveArticleResource resource) { return mapper.map(resource, Article.class); }
    private ArticleResource convertToResource(Article entity) { return mapper.map(entity, ArticleResource.class); }

    @Data
    public static class KeywordsWrapper {
        List<String> keywords;
    }

    @Data
    public static class CategoriesWrapper {
        List<String> categories;
    }
}
