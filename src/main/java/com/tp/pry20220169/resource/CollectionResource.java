package com.tp.pry20220169.resource;

import lombok.Data;

import java.util.List;

@Data
public class CollectionResource {

    private Long id;
    private String name;
    private List<ArticleResource> articles;
}
