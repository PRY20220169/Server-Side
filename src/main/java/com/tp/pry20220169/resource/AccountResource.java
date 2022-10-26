package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import lombok.Data;

@Data
public class AccountResource extends AuditModel {
    private Long id;
    private String firstName;
    private String lastName;
}
