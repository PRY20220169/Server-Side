package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ArticleService {
    Page<Article> getAllArticles(Pageable pageable);
    Article getArticleById(Long articleId);
    Article createArticle(Article article);
    Article updateArticle(Long articleId, Article article);
    ResponseEntity<?> deleteArticle(Long articleId);

    // TODO: Implement more methods

}
