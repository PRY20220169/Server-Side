package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository  extends JpaRepository<Metric, Long> {
}
