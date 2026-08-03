package com.forensicdna.service;

import com.forensicdna.entity.*;
import com.forensicdna.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {

    private final EvidenceGenotypeRepository evidenceRepo;
    private final ProfileRepository profileRepo;
    private final ProfileGenotypeRepository profileGenotypeRepo;

    public MatchService(
            EvidenceGenotypeRepository evidenceRepo,
            ProfileRepository profileRepo,
            ProfileGenotypeRepository profileGenotypeRepo
    ) {
        this.evidenceRepo = evidenceRepo;
        this.profileRepo = profileRepo;
        this.profileGenotypeRepo = profileGenotypeRepo;
    }

    public List<Map<String, Object>> matchEvidence(UUID evidenceId) {
        List<EvidenceGenotype> evidenceGenotypes = evidenceRepo.findByEvidenceId(evidenceId);

        List<Map<String, Object>> results = new ArrayList<>();

        for (Profile profile : profileRepo.findAll()) {
            List<ProfileGenotype> profileGenotypes =
                    profileGenotypeRepo.findByProfileId(profile.getId());

            int total = 0;
            int matched = 0;

            for (EvidenceGenotype eg : evidenceGenotypes) {
                for (ProfileGenotype pg : profileGenotypes) {
                    if (eg.getLocusId().equals(pg.getLocusId())) {
                        total++;

                        boolean alleleMatch =
                                eg.getAllele1().equals(pg.getAllele1()) ||
                                eg.getAllele1().equals(pg.getAllele2()) ||
                                eg.getAllele2().equals(pg.getAllele1()) ||
                                eg.getAllele2().equals(pg.getAllele2());

                        if (alleleMatch) matched++;
                    }
                }
            }

            if (total > 0) {
                double score = (double) matched / total;

                if (score > 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("profile_id", profile.getId());
                    item.put("sample_id", profile.getSampleId());
                    item.put("score", score);
                    item.put("matched_loci", matched);
                    item.put("total_loci", total);

                    results.add(item);
                }
            }
        }

        results.sort((a, b) -> Double.compare((double)b.get("score"), (double)a.get("score")));

        return results;
    }
}