package com.tp.pry20220169.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
public class Article extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "article_authors",
    joinColumns = {@JoinColumn(name = "article_id")},
    inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @JsonIgnore
    private List<Author> authors;

    public boolean hasAuthor(Author author) { return (this.getAuthors().contains(author)); }

    public Article addAuthor(Author author) {
        if(!this.hasAuthor(author)){ this.getAuthors().add(author); }
        return this;
    }

    public Article removeAuthor(Author author) {
        if(this.hasAuthor(author)){ this.getAuthors().remove(author); }
        return this;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conference_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Conference conference;

    private Date publicationDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "journal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Journal journal;

    @NotNull
    @NotBlank
    @Column(length = 500)
    private String description;

    @ElementCollection
    private List<String> keywords;

    @ElementCollection
    private List<String> categories;

    private int numberOfCitations;
    private int numberOfReferences;
}
