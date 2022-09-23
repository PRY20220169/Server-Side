package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.model.Collection;
import com.tp.pry20220169.resource.ReferenceResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CollectionService {
    Page<Collection> getAllCollections(Pageable pageable);

    Collection getCollectionById(Long collectionId);

    Collection createCollection(Collection collection);

    Collection updateCollection(Long collectionId, Collection collectionDetails);

    ResponseEntity<?> deleteCollection(Long collectionId);

    Page<Article> getAllArticlesByCollectionId(Long collectionId, Pageable pageable);

    Collection addArticleByIdAndCollectionId(Long collectionId, Long articleId);

    Collection removeArticleByIdAndCollectionId(Long collectionId, Long articleId);

    ReferenceResource getCollectionReferenceById(Long collectionId);
}
