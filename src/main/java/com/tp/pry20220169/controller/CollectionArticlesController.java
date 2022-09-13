package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Collection;
import com.tp.pry20220169.domain.service.CollectionService;
import com.tp.pry20220169.resource.CollectionResource;
import com.tp.pry20220169.resource.SaveCollectionResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Collections", description = "Collections API")
@RestController
@RequestMapping("/api/collections/{collectionId}")
@CrossOrigin
public class CollectionArticlesController {
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/articles/{articleId}")
    public CollectionResource addArticleByIdAndCollectionId(@PathVariable(name = "collectionId") Long collectionId, @PathVariable(name = "articleId") Long articleId) {
        return convertToResource(collectionService.addArticleByIdAndCollectionId(collectionId, articleId));
    }

    @DeleteMapping("/articles/{articleId}")
    public CollectionResource removeArticleByIdAndCollectionId(@PathVariable(name = "collectionId") Long collectionId, @PathVariable(name = "articleId") Long articleId) {
        return convertToResource(collectionService.removeArticleByIdAndCollectionId(collectionId, articleId));
    }

    private Collection convertToEntity(SaveCollectionResource resource) {
        return mapper.map(resource, Collection.class);
    }

    private CollectionResource convertToResource(Collection entity) {
        return mapper.map(entity, CollectionResource.class);
    }

}
