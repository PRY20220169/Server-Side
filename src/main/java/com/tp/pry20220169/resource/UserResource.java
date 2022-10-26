package com.tp.pry20220169.resource;

import com.tp.pry20220169.domain.model.AuditModel;
import lombok.Data;

@Data
public class UserResource extends AuditModel {
    private Long id;
    private String username;
    private String password;
}
