package com.tp.pry20220169.resource;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class SaveArticleResource {

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String title;

    //TODO: Implement Authors: List<Author> / Many to many relation

    //TODO: Implement Conference: Conference / Many to one relation (Conference has many articles, articles belong to one conference)
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String conferenceName;

    @NotNull
    private Date publicationDate;

    //TODO: Implement Journal: Journal / Many to one relation (Journal has many articles, articles belong to one journal)

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String journalName;

    @NotNull
    @NotBlank
    private String description;

    private List<String> keywords;
    private List<String> categories;

    @NotNull
    @Min(value = 0, message = "Number of citations value must be positive.")
    private int numberOfCitations;

    @NotNull
    @Min(value = 0, message = "Number of references value must be positive.")
    private int numberOfReferences;
}
