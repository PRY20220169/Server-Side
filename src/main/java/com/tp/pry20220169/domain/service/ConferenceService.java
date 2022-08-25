package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Conference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ConferenceService {
    Page<Conference> getAllConferences(Pageable pageable);
    Conference getConferenceById(Long conferenceId);
    Conference createConference(Conference conference);
    Conference updateConference(Long conferenceId, Conference conferenceDetails);
    ResponseEntity<?> deleteConference(Long conferenceId);
}
