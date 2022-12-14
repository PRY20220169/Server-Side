package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Journal;
import com.tp.pry20220169.domain.repository.JournalRepository;
import com.tp.pry20220169.domain.service.JournalService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import com.tp.pry20220169.resource.SaveArticleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalRepository journalRepository;

    @Override
    public Page<Journal> getAllJournals(Pageable pageable) {
        return journalRepository.findAll(pageable);
    }

    @Override
    public Journal getJournalById(Long journalId) {
        return journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "Id", journalId));
    }

    @Override
    public Journal getJournalBySaveArticleResource(SaveArticleResource resource) {
        String journalName = resource.getJournalName();
        if (journalRepository.existsByNameIgnoreCase(journalName)) return journalRepository.findByName(journalName);
        Journal journal = new Journal();
        journal.setName(journalName);
        return journalRepository.save(journal);
    }

    @Override
    public Journal createJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    @Override
    public Journal updateJournal(Long journalId, Journal journalDetails) {
        return journalRepository.findById(journalId).map(journal -> {
            journal.setName(journalDetails.getName());
            return journalRepository.save(journal);
        }).orElseThrow(() -> new ResourceNotFoundException("Journals", "Id", journalId));
    }

    @Override
    public ResponseEntity<?> deleteJournal(Long journalId) {
        return journalRepository.findById(journalId).map(journal -> {
            journalRepository.delete(journal);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Journal", "Id", journalId));
    }

    @Override
    public Page<Journal> getAllJournalsByIdList(List<Long> ids, Pageable pageable) {
        List<Journal> journalList = new ArrayList<>();
        ids.forEach(journalId -> journalList.add(journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "Id", journalId))));
        return new PageImpl<>(journalList);
    }
}
