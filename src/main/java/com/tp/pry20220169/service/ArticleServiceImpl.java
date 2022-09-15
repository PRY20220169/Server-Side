package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.repository.ArticleRepository;
import com.tp.pry20220169.domain.repository.AuthorRepository;
import com.tp.pry20220169.domain.service.ArticleService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;

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
    public List<Article> createArticleFromRPA(List<Map<String, String>> articleParamsList) {
        List<Article> newArticlesList = new ArrayList<>();

        for (Map<String, String> articleParams : articleParamsList) {
            Article newArticle = new Article();
            System.out.println(articleParams);
            // Set Article Title
            newArticle.setTitle(articleParams.get("Article_Title"));

            // Set Article Date
//            String dateInString = articleParams.get("date"); //Format: Mar 2013
//            dateInString = dateInString.replaceAll("[^0-9a-zA-Z:,]+", "");
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
//            LocalDate localDate = LocalDate.parse(dateInString, formatter);
//            Date dateTime = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//            newArticle.setPublicationDate(dateTime);

            // Set Article Description
            newArticle.setDescription(articleParams.get("summary"));

            // Set Article Metrics
            String metricsString = articleParams.get("metrics").replaceAll("\\n", ",");
            List<String> metricsList = Arrays.asList(metricsString.split("\\s*,\\s*"));
            int numberOfCitations = Integer.parseInt(metricsList.get(0));
            int numberOfReferences = Integer.parseInt(metricsList.get(2));

            newArticle.setNumberOfCitations(numberOfCitations);
            newArticle.setNumberOfReferences(numberOfReferences);

            newArticlesList.add(newArticle);
            articleRepository.save(newArticle);

            //TODO: Set Authors, Conference, Journal, Keywords, and Categories
            //TODO: Handle errors
        }
        return newArticlesList;
    }

    @Override
    public Article updateArticle(Long articleId, Article articleDetails) {
        return articleRepository.findById(articleId).map(article -> {
            article.setTitle(articleDetails.getTitle());
            article.setConference(articleDetails.getConference());
            article.setPublicationDate(articleDetails.getPublicationDate());
            article.setJournal(articleDetails.getJournal());
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

    @Override
    public Article addArticleAuthor(Long articleId, Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
        return articleRepository.findById(articleId).map(article ->
                articleRepository.save(article.addAuthor(author)))
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Article removeArticleAuthor(Long articleId, Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
        return articleRepository.findById(articleId).map(article ->
                articleRepository.save(article.removeAuthor(author)))
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Page<Article> getAllArticlesByAuthorId(Long authorId, Pageable pageable) {
        return authorRepository.findById(authorId).map(author -> {
            List<Article> articles = author.getArticles();
            int articlesCount = articles.size();
            return new PageImpl<>(articles, pageable, articlesCount);
        }).orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
    }

    @Override
    public Page<Article> getAllArticlesByConferenceId(Long conferenceId, Pageable pageable) {
        return articleRepository.findAllByConferenceId(conferenceId, pageable);
    }

    @Override
    public Page<Article> getAllArticlesByJournalId(Long journalId, Pageable pageable) {
        return articleRepository.findAllByJournalId(journalId, pageable);
    }

    @Override
    public Page<Article> getAllArticlesByKeywords(List<String> keywords, Pageable pageable) {
        return articleRepository.findByKeywordsIn(keywords, pageable);
    }

    @Override
    public Page<Article> getAllArticlesByCategories(List<String> categories, Pageable pageable) {
        return articleRepository.findByCategoriesIn(categories, pageable);
    }
}
