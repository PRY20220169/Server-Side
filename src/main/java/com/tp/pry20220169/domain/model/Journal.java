package com.tp.pry20220169.domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "journals")
@Data
public class Journal extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id")
    private List<Metric> metrics;

    //TODO: Expand journal attributes to fit Scimago input

    private Long scimagoId = 0L;

    private String issn = "";

    private String country = "";

    private String publisher = "";

    @Column(columnDefinition = "TEXT")
    private String categories = "";

}
