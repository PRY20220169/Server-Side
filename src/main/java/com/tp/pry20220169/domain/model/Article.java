package com.tp.pry20220169.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "articles")
@Data
@Builder
@AllArgsConstructor
public class Article extends AuditModel {
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

    @ManyToMany(mappedBy = "articles")
    private List<Collection> collections;

    public Article() {
    }

    public boolean hasAuthor(Author author) {
        return (this.getAuthors().contains(author));
    }

    public Article addAuthor(Author author) {
        if (!this.hasAuthor(author)) {
            this.getAuthors().add(author);
        }
        return this;
    }

    public Article removeAuthor(Author author) {
        if (this.hasAuthor(author)) {
            this.getAuthors().remove(author);
        }
        return this;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Conference conference;

    private Date publicationDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "journal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Journal journal;

    @NotNull
    @NotBlank
    @Column(length = 2000)
    private String description;

    @ElementCollection
    private List<String> keywords;

    @ElementCollection
    private List<String> categories;

    private int numberOfCitations;
    private int numberOfReferences;

    public String getReference() {
        //Authors
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.getAuthors().size(); i++) {
            Author author = getAuthors().get(i);
            stringBuilder.append(author.getLastName());
            stringBuilder.append(", ");
            stringBuilder.append(author.getFirstName().toCharArray()[0]);
            if (i + 2 == getAuthors().size()) stringBuilder.append(". & ");
            else if (i + 1 == getAuthors().size()) stringBuilder.append(". ");
            else stringBuilder.append("., ");
        }

        //Year
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(this.getPublicationDate());
        int year = calendar.get(Calendar.YEAR);
        stringBuilder.append("(");
        stringBuilder.append(year);
        stringBuilder.append("). ");

        //Title
        stringBuilder.append(this.getTitle());
        stringBuilder.append(". ");

        //Journal
        String input = this.getJournal().getName();
        String journalStr = Arrays.stream(input.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
        stringBuilder.append(journalStr);
        stringBuilder.append(".");

        return stringBuilder.toString();
    }
}
