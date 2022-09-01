package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    Optional<Conference> findByMeetingName(String meetingName);
}
