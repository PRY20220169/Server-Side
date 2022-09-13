package com.tp.pry20220169.resource;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SaveCollectionResource {

    @NotBlank
    @Size(max = 100)
    private String name;

}


