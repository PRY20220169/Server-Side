package com.tp.pry20220169.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "metrics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metric extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String bibliometric;

    @NotNull
    @NotBlank
    private String score;

    private int metricYear;

    @NotNull
    @NotBlank
    private String source;

}
