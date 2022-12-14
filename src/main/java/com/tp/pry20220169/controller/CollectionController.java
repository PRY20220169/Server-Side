package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Collection;
import com.tp.pry20220169.domain.service.CollectionService;
import com.tp.pry20220169.resource.CollectionResource;
import com.tp.pry20220169.resource.ReferenceResource;
import com.tp.pry20220169.resource.SaveCollectionResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Collections", description = "Collections API")
@RestController
@RequestMapping("/api/collections")
@CrossOrigin
public class CollectionController {
    
    @Autowired
    private CollectionService collectionService;
    
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/{collectionId}")
    public CollectionResource getCollectionById(@PathVariable(name = "collectionId") Long collectionId){
        return convertToResource(collectionService.getCollectionById(collectionId));
    }

    @PutMapping("/{collectionId}")
    public CollectionResource updateCollection(@PathVariable(name = "collectionId") Long collectionId,
                                       @Valid @RequestBody SaveCollectionResource resource){
        Collection collection = convertToEntity(resource);
        return convertToResource(collectionService.updateCollection(collectionId, collection));
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<?> deleteCollection(@PathVariable(name = "collectionId") Long collectionId){
        return collectionService.deleteCollection(collectionId);
    }

    @GetMapping("/{collectionId}/reference")
    public ReferenceResource getCollectionReferenceById(@PathVariable(name = "collectionId") Long collectionId){
        return collectionService.getCollectionReferenceById(collectionId);
    }
    
    private Collection convertToEntity(SaveCollectionResource resource) { return mapper.map(resource, Collection.class); }
    private CollectionResource convertToResource(Collection entity) { return mapper.map(entity, CollectionResource.class); }


}
