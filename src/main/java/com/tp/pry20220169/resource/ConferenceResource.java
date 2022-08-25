package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import lombok.Data;

import java.util.Date;

@Data
public class ConferenceResource extends AuditModel {

    private Long id;
    private String meetingName;
    private Date date;

}
