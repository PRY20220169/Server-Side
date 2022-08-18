package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Bibliometric;
import com.tp.pry20220169.domain.repository.BibliometricRepository;
import com.tp.pry20220169.domain.service.BibliometricService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BibliometricServiceImpl implements BibliometricService {
    @Autowired
    private BibliometricRepository bibliometricRepository;

    @Override
    public Page<Bibliometric> getAllBibliometrics(Pageable pageable) {
        return bibliometricRepository.findAll(pageable);
    }

    @Override
    public Bibliometric getBibliometricById(Long bibliometricId) {
        return bibliometricRepository.findById(bibliometricId)
                .orElseThrow(() -> new ResourceNotFoundException("Bibliometric", "Id", bibliometricId));
    }

    @Override
    public Bibliometric createBibliometric(Bibliometric bibliometric) {
        return bibliometricRepository.save(bibliometric);
    }

    @Override
    public Bibliometric updateBibliometric(Long bibliometricId, Bibliometric bibliometricDetails) {
        return bibliometricRepository.findById(bibliometricId).map(bibliometric -> {
            bibliometric.setName(bibliometricDetails.getName());
            return bibliometricRepository.save(bibliometric);
        }).orElseThrow(() -> new ResourceNotFoundException("Bibliometric", "Id", bibliometricId));
    }

    @Override
    public ResponseEntity<?> deleteBibliometric(Long bibliometricId) {
        return bibliometricRepository.findById(bibliometricId).map(bibliometric -> {
            bibliometricRepository.delete(bibliometric);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Bibliometric", "Id", bibliometricId));
    }
}
