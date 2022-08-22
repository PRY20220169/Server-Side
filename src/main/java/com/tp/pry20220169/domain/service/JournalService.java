package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface JournalService {
    Page<Journal> getAllJournals(Pageable pageable);
    Journal getJournalById(Long journalId);
    Journal createJournal(Journal journal);
    Journal updateJournal(Long journalId, Journal journalDetails);
    ResponseEntity<?> deleteJournal(Long journalId);
}
