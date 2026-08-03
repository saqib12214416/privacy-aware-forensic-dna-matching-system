package com.forensicdna.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forensicdna.dto.*;
import com.forensicdna.entity.*;
import com.forensicdna.repository.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class EvidenceService {

    private final EvidenceRepository evidenceRepo;
    private final EvidenceGenotypeRepository genotypeRepo;
    private final StrLocusRepository locusRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public EvidenceService(
            EvidenceRepository evidenceRepo,
            EvidenceGenotypeRepository genotypeRepo,
            StrLocusRepository locusRepo
    ) {
        this.evidenceRepo = evidenceRepo;
        this.genotypeRepo = genotypeRepo;
        this.locusRepo = locusRepo;
    }

    public Evidence submitEvidence(EvidenceRequest req, String userId) throws Exception {
        UUID evidenceId = UUID.randomUUID();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("sample_id", req.getSample_id());
        metadata.put("population", req.getPopulation());
        metadata.put("notes", req.getNotes());
        metadata.put("genotypes", req.getGenotypes());
        metadata.put("submitted_at", OffsetDateTime.now().toString());

        Evidence evidence = new Evidence();
        evidence.setId(evidenceId);
        evidence.setEvidenceCode(req.getSample_id() != null ? req.getSample_id() : evidenceId.toString());
        evidence.setSubmittedBy(UUID.fromString(userId));
        evidence.setReceivedAt(OffsetDateTime.now());
        evidence.setMetadata(mapper.writeValueAsString(metadata));

        evidenceRepo.save(evidence);

        for (GenotypeRequest g : req.getGenotypes()) {
            StrLocus locus = locusRepo.findByLocus(g.getLocus())
                    .orElseThrow(() -> new RuntimeException("Unknown locus: " + g.getLocus()));

            EvidenceGenotype eg = new EvidenceGenotype();
            eg.setEvidenceId(evidenceId);
            eg.setLocusId(locus.getId());
            eg.setAllele1(g.getAllele1());
            eg.setAllele2(g.getAllele2());

            genotypeRepo.save(eg);
        }

        return evidence;
    }

    public List<Evidence> listEvidence() {
        return evidenceRepo.findAll();
    }

    public Evidence getEvidence(UUID id) {
        return evidenceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Evidence not found"));
    }
}