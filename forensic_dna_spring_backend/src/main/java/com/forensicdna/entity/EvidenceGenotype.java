package com.forensicdna.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "evidence_genotypes")
public class EvidenceGenotype {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "evidence_id")
    private UUID evidenceId;

    @Column(name = "locus_id")
    private Integer locusId;

    private String allele1;
    private String allele2;

    public Integer getId() { return id; }
    public UUID getEvidenceId() { return evidenceId; }
    public void setEvidenceId(UUID evidenceId) { this.evidenceId = evidenceId; }

    public Integer getLocusId() { return locusId; }
    public void setLocusId(Integer locusId) { this.locusId = locusId; }

    public String getAllele1() { return allele1; }
    public void setAllele1(String allele1) { this.allele1 = allele1; }

    public String getAllele2() { return allele2; }
    public void setAllele2(String allele2) { this.allele2 = allele2; }
}