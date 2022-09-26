package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Collection;
import com.tp.pry20220169.domain.service.CollectionService;
import com.tp.pry20220169.resource.CollectionResource;
import com.tp.pry20220169.resource.SaveCollectionResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Account", description = "Account API")
@RestController
@RequestMapping("/api/users/{userId}/account/collections")
@CrossOrigin
public class AccountCollectionsController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public Page<CollectionResource> getAllCollectionsByUserId(@PathVariable Long userId, Pageable pageable) {
        Page<Collection> collectionPage = collectionService.getAllCollectionsByUserId(userId, pageable);
        List<CollectionResource> resources = collectionPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @PostMapping
    public CollectionResource createCollection(@PathVariable Long userId, @Valid @RequestBody SaveCollectionResource resource) {
        Collection collection = convertToEntity(resource);
        return convertToResource(collectionService.createCollection(userId, collection));
    }

    private Collection convertToEntity(SaveCollectionResource resource) {
        return mapper.map(resource, Collection.class);
    }

    private CollectionResource convertToResource(Collection entity) {
        return mapper.map(entity, CollectionResource.class);
    }

}
