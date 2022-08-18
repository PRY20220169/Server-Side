package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Metric;
import com.tp.pry20220169.domain.repository.MetricRepository;
import com.tp.pry20220169.domain.service.MetricService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MetricServiceImpl implements MetricService {
    @Autowired
    private MetricRepository metricRepository;

    @Override
    public Page<Metric> getAllMetrics(Pageable pageable) {
        return metricRepository.findAll(pageable);
    }

    @Override
    public Metric getMetricById(Long metricId) {
        return metricRepository.findById(metricId)
                .orElseThrow(() -> new ResourceNotFoundException("Metric", "Id", metricId));
    }

    @Override
    public Metric createMetric(Metric metric) {
        return metricRepository.save(metric);
    }

    @Override
    public Metric updateMetric(Long metricId, Metric metricDetails) {
        return metricRepository.findById(metricId).map(metric -> {
            metric.setBibliometric(metricDetails.getBibliometric());
            metric.setScore(metricDetails.getScore());
            metric.setYear(metricDetails.getYear());
            metric.setSource(metricDetails.getSource());
            return metricRepository.save(metric);
        }).orElseThrow(() -> new ResourceNotFoundException("Metric", "Id", metricId));
    }

    @Override
    public ResponseEntity<?> deleteMetric(Long metricId) {
        return metricRepository.findById(metricId).map(metric -> {
            metricRepository.delete(metric);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Metric", "Id", metricId));
    }
}
