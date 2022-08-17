package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ArticleService {
    Page<Article> getAllArticles(Pageable pageable);
    Article createArticle(Article article);
    Article updateArticle(Long id, Article article);
    ResponseEntity<?> deleteArticle(Long id);

    // TODO: Implement more methods

}
