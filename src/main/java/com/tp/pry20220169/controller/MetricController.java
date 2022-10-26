package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.model.Metric;
import com.tp.pry20220169.domain.service.MetricService;
import com.tp.pry20220169.resource.ArticleResource;
import com.tp.pry20220169.resource.MetricResource;
import com.tp.pry20220169.resource.SaveArticleResource;
import com.tp.pry20220169.resource.SaveMetricResource;
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

@Tag(name = "Metrics", description = "Metrics API")
@RestController
@RequestMapping("/api/metrics")
@CrossOrigin
public class MetricController {

    @Autowired
    private MetricService metricService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<MetricResource> getAllMetrics(Pageable pageable){
        Page<Metric> metricPage = metricService.getAllMetrics(pageable);
        List<MetricResource> resources = metricPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/{metricId}")
    public MetricResource getMetricById(@PathVariable(name = "metricId") Long metricId){
        return convertToResource(metricService.getMetricById(metricId));
    }

    @PostMapping("")
    public MetricResource createMetric(@Valid @RequestBody SaveMetricResource resource){
        Metric metric = convertToEntity(resource);
        return convertToResource(metricService.createMetric(metric));
    }

    @PutMapping("/{metricId}")
    public MetricResource updateMetric(@PathVariable(name = "metricId") Long metricId,
                                         @Valid @RequestBody SaveMetricResource resource){
        Metric metric = convertToEntity(resource);
        return convertToResource(metricService.updateMetric(metricId, metric));
    }

    @DeleteMapping("/{metricId}")
    public ResponseEntity<?> deleteMetric(@PathVariable(name = "metricId") Long metricId){
        return metricService.deleteMetric(metricId);
    }

    private Metric convertToEntity(SaveMetricResource resource) { return mapper.map(resource, Metric.class); }
    private MetricResource convertToResource(Metric entity) { return mapper.map(entity, MetricResource.class); }
}
