package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import com.tp.pry20220169.domain.model.Metric;
import lombok.Data;

import java.util.List;

@Data
public class JournalResource extends AuditModel {

    private Long id;
    private String name;
    private List<Metric> metrics;

}
