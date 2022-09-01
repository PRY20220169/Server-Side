package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.service.ArticleService;
import com.tp.pry20220169.resource.ArticleResource;
import com.tp.pry20220169.resource.SaveArticleResource;
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
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Articles", description = "Articles API")
@RestController
@RequestMapping("/api/articles")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<ArticleResource> getAllArticles(Pageable pageable){
        Page<Article> articlePage = articleService.getAllArticles(pageable);
        List<ArticleResource> resources = articlePage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/{articleId}")
    public ArticleResource getArticleById(@PathVariable(name = "articleId") Long articleId){
        return convertToResource(articleService.getArticleById(articleId));
    }

    @PostMapping("")
    public ArticleResource createArticle(@Valid @RequestBody SaveArticleResource resource){
        Article article = convertToEntity(resource);
        return convertToResource(articleService.createArticle(article));
    }

    //RPA Test Connection Endpoint
    @PostMapping("/rpa-test")
    public List<Article> createArticleRPATest(@Valid @RequestBody List<Map<String, String>> resource){
        return articleService.createArticleFromRPA(resource);
    }

    @PutMapping("/{articleId}")
    public ArticleResource updateArticle(@PathVariable(name = "articleId") Long articleId,
            @Valid @RequestBody SaveArticleResource resource){
        Article article = convertToEntity(resource);
        return convertToResource(articleService.updateArticle(articleId, article));
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable(name = "articleId") Long articleId){
        return articleService.deleteArticle(articleId);
    }

    private Article convertToEntity(SaveArticleResource resource) { return mapper.map(resource, Article.class); }
    private ArticleResource convertToResource(Article entity) { return mapper.map(entity, ArticleResource.class); }
}
