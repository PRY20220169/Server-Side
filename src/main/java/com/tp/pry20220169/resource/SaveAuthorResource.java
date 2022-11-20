package com.tp.pry20220169.resource;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class SaveAuthorResource {

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(max = 200)
    private String address;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    private List<String> organizations;

    private List<SaveMetricResource> metrics;

}
