package com.tp.pry20220169.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
public class Account extends AuditModel {
    @Id
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<Collection> collections = new ArrayList<>();

    public Account addCollection(Collection collection) {
        collections.add(collection);
        return this;
    }

    public Account removeCollection(Collection collection) {
        collections.remove(collection);
        return this;
    }
}
