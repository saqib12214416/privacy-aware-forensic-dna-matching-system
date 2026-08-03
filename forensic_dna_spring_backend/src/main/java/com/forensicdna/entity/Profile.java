package com.forensicdna.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    private UUID id;

    @Column(name = "sample_id")
    private String sampleId;

    @Column(name = "population_id")
    private Integer populationId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    private String notes;

    public UUID getId() { return id; }
    public String getSampleId() { return sampleId; }
    public Integer getPopulationId() { return populationId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public String getNotes() { return notes; }
}