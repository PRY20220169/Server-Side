package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Journal;
import com.tp.pry20220169.domain.service.JournalService;
import com.tp.pry20220169.resource.JournalResource;
import com.tp.pry20220169.resource.SaveJournalResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Compare", description = "Compare API")
@RestController
@RequestMapping("/api/journals/compare")
@CrossOrigin
public class JournalCompare {

    @Autowired
    private JournalService journalService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<JournalResource> compareJournalsById(@Valid @RequestBody IdListWrapper idList, Pageable pageable){
        Page<Journal> journalPage = journalService.getAllJournalsByIdList(idList.getIds(), pageable);
        List<JournalResource> resources = journalPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @PostMapping("")
    public Page<JournalResource> compareJournalsByIdPost(@Valid @RequestBody IdListWrapper idList, Pageable pageable){
        Page<Journal> journalPage = journalService.getAllJournalsByIdList(idList.getIds(), pageable);
        List<JournalResource> resources = journalPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    private Journal convertToEntity(SaveJournalResource resource) { return mapper.map(resource, Journal.class); }
    private JournalResource convertToResource(Journal entity) { return mapper.map(entity, JournalResource.class); }

    @Data
    public static class IdListWrapper {
        List<Long> ids;
    }
}
