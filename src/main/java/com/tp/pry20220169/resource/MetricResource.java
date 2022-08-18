package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import com.tp.pry20220169.domain.model.Bibliometric;
import lombok.Data;

@Data
public class MetricResource extends AuditModel {

    private Long id;
    private Bibliometric bibliometric;
    private String score;
    private int year;
    private String source;

}
