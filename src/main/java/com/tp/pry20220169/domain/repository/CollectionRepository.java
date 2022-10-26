package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Page<Collection> findCollectionsByAccountId(Long id, Pageable pageable);

}
