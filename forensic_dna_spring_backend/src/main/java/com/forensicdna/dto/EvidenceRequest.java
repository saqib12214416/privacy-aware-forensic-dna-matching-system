package com.forensicdna.dto;

import java.util.List;

public class EvidenceRequest {
    private String sample_id;
    private String population;
    private String notes;
    private List<GenotypeRequest> genotypes;

    public String getSample_id() { return sample_id; }
    public String getPopulation() { return population; }
    public String getNotes() { return notes; }
    public List<GenotypeRequest> getGenotypes() { return genotypes; }
}