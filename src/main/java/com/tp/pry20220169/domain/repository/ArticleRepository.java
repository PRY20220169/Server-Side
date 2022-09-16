package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByConferenceId(Long conferenceId, Pageable pageable);
    Page<Article> findAllByJournalId(Long journalId, Pageable pageable);
    Article findTopByOrderByIdDesc();
    Page<Article> findByKeywordsIn(List<String> keywords, Pageable pageable);
    Page<Article> findByCategoriesIn(List<String> categories, Pageable pageable);
}
