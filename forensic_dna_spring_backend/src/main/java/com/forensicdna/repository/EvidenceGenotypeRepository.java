package com.forensicdna.repository;

import com.forensicdna.entity.EvidenceGenotype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface EvidenceGenotypeRepository extends JpaRepository<EvidenceGenotype, Integer> {
    List<EvidenceGenotype> findByEvidenceId(UUID evidenceId);
}