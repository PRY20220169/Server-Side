package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Conference;
import com.tp.pry20220169.domain.repository.ConferenceRepository;
import com.tp.pry20220169.domain.service.ConferenceService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ConferenceServiceImpl implements ConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Override
    public Page<Conference> getAllConferences(Pageable pageable) { return conferenceRepository.findAll(pageable); }

    @Override
    public Conference getConferenceById(Long conferenceId) {
        return conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Conference", "Id", conferenceId));
    }

    @Override
    public Conference createConference(Conference conference) { return conferenceRepository.save(conference); }

    @Override
    public Conference updateConference(Long conferenceId, Conference conferenceDetails) {
        return conferenceRepository.findById(conferenceId).map(conference -> {
            conference.setMeetingName(conferenceDetails.getMeetingName());
            conference.setDate(conferenceDetails.getDate());
            return conferenceRepository.save(conference);
        }).orElseThrow(() -> new ResourceNotFoundException("Conference", "Id", conferenceId));
    }

    @Override
    public ResponseEntity<?> deleteConference(Long conferenceId) {
        return conferenceRepository.findById(conferenceId).map(conference -> {
            conferenceRepository.delete(conference);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Conference", "Id", conferenceId));
    }
}
