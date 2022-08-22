package com.tp.pry20220169.resource;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SaveJournalResource {

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;

    private List<SaveMetricResource> metrics;

}
