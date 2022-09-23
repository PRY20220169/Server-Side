package com.tp.pry20220169.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
@Data
public class Collection extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "articles_collection",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    private List<Article> articles = new ArrayList<>();

    public Collection addArticle(Article article) {
        articles.add(article);
        return this;
    }

    public Collection removeArticle(Article article) {
        articles.remove(article);
        return this;
    }

    public String getReference() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<this.getArticles().size(); i++) {
            Article article = this.getArticles().get(i);
            stringBuilder.append(article.getReference());
            if (i+1==this.getArticles().size()) break;
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
