package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import com.tp.pry20220169.domain.model.Metric;
import lombok.Data;

import java.util.List;

@Data
public class AuthorResource extends AuditModel {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private List<String> organizations;
    private List<Metric> metrics;

}
