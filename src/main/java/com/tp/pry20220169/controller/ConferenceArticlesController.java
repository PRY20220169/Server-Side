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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Conferences", description = "Conferences API")
@RestController
@RequestMapping("/api/conferences/{conferenceId}")
@CrossOrigin
public class ConferenceArticlesController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/articles")
    public Page<ArticleResource> getAllArticlesByConferenceId(@PathVariable(name = "conferenceId") Long conferenceId, Pageable pageable){
        Page<Article> articlePage = articleService.getAllArticlesByConferenceId(conferenceId, pageable);
        List<ArticleResource> resources = articlePage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    private Article convertToEntity(SaveArticleResource resource) { return mapper.map(resource, Article.class); }
    private ArticleResource convertToResource(Article entity) { return mapper.map(entity, ArticleResource.class); }
}
