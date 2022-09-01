package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ArticleService {
    Page<Article> getAllArticles(Pageable pageable);
    Article getArticleById(Long articleId);
    Article createArticle(Article article);
    Article updateArticle(Long articleId, Article articleDetails);
    ResponseEntity<?> deleteArticle(Long articleId);

    Article addArticleAuthor(Long articleId, Long authorId);
    Article removeArticleAuthor(Long articleId, Long authorId);
    Page<Article> getAllArticlesByAuthorId(Long authorId, Pageable pageable);
    Page<Article> getAllArticlesByConferenceId(Long conferenceId, Pageable pageable);
    // TODO: Implement more methods

}
