package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.model.Journal;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleResource extends AuditModel {

    private Long id;
    private String title;
    private Journal journal;
    private List<Author> authors;
    // private Conference conference;
    private Date publicationDate;
    private String description;
    private List<String> keywords;
    private List<String> categories;
    private int numberOfCitations;
    private int numberOfReferences;

}
