package com.tp.pry20220169.resource;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class SaveJournalResource {

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;

    private List<SaveMetricResource> metrics;

    private Long scimagoId;
    private String issn;
    private String country;
    private String publisher;
    private String categories;

}
