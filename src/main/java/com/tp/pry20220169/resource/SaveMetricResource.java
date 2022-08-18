package com.tp.pry20220169.resource;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SaveMetricResource {

    @NotNull
    @NotBlank
    @Size(max = 5)
    private String score;

    @NotNull
    @Min(value = 0, message = "Year value must be positive.")
    private int year;

    @NotNull
    @NotBlank
    private String source;

}
