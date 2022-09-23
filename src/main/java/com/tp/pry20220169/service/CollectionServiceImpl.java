package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.model.Collection;
import com.tp.pry20220169.domain.repository.ArticleRepository;
import com.tp.pry20220169.domain.repository.CollectionRepository;
import com.tp.pry20220169.domain.service.CollectionService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import com.tp.pry20220169.resource.ReferenceResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Page<Collection> getAllCollections(Pageable pageable) {
        return collectionRepository.findAll(pageable);
    }

    @Override
    public Collection getCollectionById(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "Id", collectionId));
    }

    @Override
    public Collection createCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    @Override
    public Collection updateCollection(Long collectionId, Collection collectionDetails) {
        return collectionRepository.findById(collectionId)
                .map(collection -> {
                    collection.setName(collection.getName());
                    collection.setArticles(collection.getArticles());
                    return collectionRepository.save(collection);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "Id", collectionId));
    }

    @Override
    public ResponseEntity<?> deleteCollection(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .map(collection -> {
                    collectionRepository.delete(collection);
                    return ResponseEntity.ok().build();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "Id", collectionId));
    }

    @Override
    public Page<Article> getAllArticlesByCollectionId(Long collectionId, Pageable pageable) {
        return collectionRepository.findById(collectionId)
                .map(collection -> new PageImpl<>(collection.getArticles(), pageable, collection.getArticles().size()))
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "Id", collectionId));
    }

    @Override
    public Collection addArticleByIdAndCollectionId(Long collectionId, Long articleId) {
        return articleRepository.findById(articleId)
                .map(article -> collectionRepository.findById(collectionId)
                        .map(collection -> collectionRepository.save(collection.addArticle(article)))
                        .orElseThrow(() -> new ResourceNotFoundException("Collection", "Id", collectionId)))
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Collection removeArticleByIdAndCollectionId(Long collectionId, Long articleId) {
        return articleRepository.findById(articleId)
                .map(article -> collectionRepository.findById(collectionId)
                        .map(collection -> collectionRepository.save(collection.removeArticle(article)))
                        .orElseThrow(() -> new ResourceNotFoundException("Collection", "Id", collectionId)))
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public ReferenceResource getCollectionReferenceById(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection", "Id", collectionId));
        ReferenceResource resource = new ReferenceResource();
        resource.setReference(collection.getReference());
        return resource;
    }


}
