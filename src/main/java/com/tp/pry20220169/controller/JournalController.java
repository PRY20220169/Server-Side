package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Journal;
import com.tp.pry20220169.domain.service.JournalService;
import com.tp.pry20220169.resource.JournalResource;
import com.tp.pry20220169.resource.SaveJournalResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Journals", description = "Journals API")
@RestController
@RequestMapping("/api/journals")
@CrossOrigin
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<JournalResource> getAllJournals(Pageable pageable){
        Page<Journal> journalPage = journalService.getAllJournals(pageable);
        List<JournalResource> resources = journalPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/{journalId}")
    public JournalResource getJournalById(@PathVariable(name = "journalId") Long journalId){
        return convertToResource(journalService.getJournalById(journalId));
    }

    @PostMapping("")
    public JournalResource createJournal(@Valid @RequestBody SaveJournalResource resource){
        Journal journal = convertToEntity(resource);
        return convertToResource(journalService.createJournal(journal));
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulkCreateJournal(@Valid @RequestBody List<SaveJournalResource> resource){
        for (SaveJournalResource r: resource) {
            Journal journal = convertToEntity(r);
            journalService.createJournal(journal);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{journalId}")
    public JournalResource updateJournal(@PathVariable(name = "journalId") Long journalId,
                                       @Valid @RequestBody SaveJournalResource resource){
        Journal journal = convertToEntity(resource);
        return convertToResource(journalService.updateJournal(journalId, journal));
    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<?> deleteJournal(@PathVariable(name = "journalId") Long journalId){
        return journalService.deleteJournal(journalId);
    }

    private Journal convertToEntity(SaveJournalResource resource) { return mapper.map(resource, Journal.class); }
    private JournalResource convertToResource(Journal entity) { return mapper.map(entity, JournalResource.class); }
}
