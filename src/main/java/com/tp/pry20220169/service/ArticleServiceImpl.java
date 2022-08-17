package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.repository.ArticleRepository;
import com.tp.pry20220169.domain.service.ArticleService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Page<Article> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long articleId, Article articleDetails) {
        return articleRepository.findById(articleId).map(article -> {
            article.setTitle(articleDetails.getTitle());
            //article.setAuthors
            //article.setConference
            article.setPublicationDate(articleDetails.getPublicationDate());
            //article.setJournal
            article.setDescription(articleDetails.getDescription());
            article.setKeywords(articleDetails.getKeywords());
            article.setCategories(articleDetails.getCategories());
            article.setNumberOfCitations(articleDetails.getNumberOfCitations());
            article.setNumberOfReferences(articleDetails.getNumberOfReferences());
            return articleRepository.save(article);
        }).orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public ResponseEntity<?> deleteArticle(Long articleId) {
        return articleRepository.findById(articleId).map(article -> {
            articleRepository.delete(article);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }
}
