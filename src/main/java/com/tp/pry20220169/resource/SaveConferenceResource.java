package com.tp.pry20220169.resource;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class SaveConferenceResource {

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String meetingName;

    @NotNull
    private Date date;

}
