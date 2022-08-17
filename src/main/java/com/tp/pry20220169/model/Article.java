package com.tp.pry20220169.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    //TODO: Implement Authors: List<Author> / Many to many relation

    //TODO: Implement Conference: Conference / Many to one relation (Conference has many articles, articles belong to one conference)

    @NotNull
    private Date publicationDate;

    //TODO: Implement Journal: Journal / Many to one relation (Journal has many articles, articles belong to one journal)

    @NotNull
    @NotBlank
    private String description;

    @ElementCollection
    private List<String> keywords;

    @ElementCollection
    private List<String> categories;

    private int numberOfCitations;
    private int numberOfReferences;
}
