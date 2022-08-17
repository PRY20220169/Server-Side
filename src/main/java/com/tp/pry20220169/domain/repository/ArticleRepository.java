package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    // find by conference id, etc
}
