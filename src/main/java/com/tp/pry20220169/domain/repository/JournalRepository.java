package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    Boolean existsByNameIgnoreCase(String name);
    Journal findByName(String name);
}
