package com.forensicdna.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "profile_genotypes")
public class ProfileGenotype {

    @Id
    private Integer id;

    @Column(name = "profile_id")
    private UUID profileId;

    @Column(name = "locus_id")
    private Integer locusId;

    private String allele1;
    private String allele2;

    public Integer getId() { return id; }
    public UUID getProfileId() { return profileId; }
    public Integer getLocusId() { return locusId; }
    public String getAllele1() { return allele1; }
    public String getAllele2() { return allele2; }
}