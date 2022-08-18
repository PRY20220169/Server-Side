package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Bibliometric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BibliometricService {
    Page<Bibliometric> getAllBibliometrics(Pageable pageable);
    Bibliometric getBibliometricById(Long bibliometricId);
    Bibliometric createBibliometric(Bibliometric bibliometric);
    Bibliometric updateBibliometric(Long bibliometricId, Bibliometric bibliometric);
    ResponseEntity<?> deleteBibliometric(Long bibliometricId);
}
