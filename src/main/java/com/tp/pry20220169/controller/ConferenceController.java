package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Conference;
import com.tp.pry20220169.domain.service.ConferenceService;
import com.tp.pry20220169.resource.ConferenceResource;
import com.tp.pry20220169.resource.SaveConferenceResource;
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

@Tag(name = "Conferences", description = "Conferences API")
@RestController
@RequestMapping("/api/conferences")
@CrossOrigin
public class ConferenceController {
    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<ConferenceResource> getAllConferences(Pageable pageable){
        Page<Conference> conferencePage = conferenceService.getAllConferences(pageable);
        List<ConferenceResource> resources = conferencePage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/{conferenceId}")
    public ConferenceResource getConferenceById(@PathVariable(name = "conferenceId") Long conferenceId){
        return convertToResource(conferenceService.getConferenceById(conferenceId));
    }

    @PostMapping("")
    public ConferenceResource createConference(@Valid @RequestBody SaveConferenceResource resource){
        Conference conference = convertToEntity(resource);
        return convertToResource(conferenceService.createConference(conference));
    }

    @PutMapping("/{conferenceId}")
    public ConferenceResource updateConference(@PathVariable(name = "conferenceId") Long conferenceId,
                                       @Valid @RequestBody SaveConferenceResource resource){
        Conference conference = convertToEntity(resource);
        return convertToResource(conferenceService.updateConference(conferenceId, conference));
    }

    @DeleteMapping("/{conferenceId}")
    public ResponseEntity<?> deleteConference(@PathVariable(name = "conferenceId") Long conferenceId){
        return conferenceService.deleteConference(conferenceId);
    }

    private Conference convertToEntity(SaveConferenceResource resource) { return mapper.map(resource, Conference.class); }
    private ConferenceResource convertToResource(Conference entity) { return mapper.map(entity, ConferenceResource.class); }
}
