package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Conference;
import com.tp.pry20220169.domain.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByName(String journalName);
}
