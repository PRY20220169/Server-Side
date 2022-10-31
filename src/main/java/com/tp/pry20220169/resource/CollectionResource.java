package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionResource extends AuditModel {

    private Long id;
    private String name;
    private List<ArticleResource> articles;

}
