package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface MetricService {
    Page<Metric> getAllMetrics(Pageable pageable);
    Metric getMetricById(Long metricId);
    Metric createMetric(Metric metric);
    Metric updateMetric(Long metricId, Metric metricDetails);
    ResponseEntity<?> deleteMetric(Long metricId);
}
