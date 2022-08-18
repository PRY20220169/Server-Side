package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import lombok.Data;

@Data
public class MetricResource extends AuditModel {

    private Long id;
    private String bibliometric;
    private String score;
    private int year;
    private String source;

}
