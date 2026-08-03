package com.forensicdna.service;

import com.opencsv.CSVReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CsvImportService {

    private final JdbcTemplate jdbcTemplate;

    public CsvImportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String importCombinedProfiles(String filePath) throws Exception {

        String[] lociColumns = {
                "FGA",
                "TPO",
                "D1S1609",
                "D2S441",
                "D8S1108",
                "D21S2052",
                "D18S51"
        };

        int profileCount = 0;
        int genotypeCount = 0;
        int skippedCount = 0;

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {

            String[] headers = reader.readNext();

            if (headers == null) {
                throw new RuntimeException("CSV file is empty");
            }

            Map<String, Integer> columnIndex = new HashMap<>();

            for (int i = 0; i < headers.length; i++) {
                columnIndex.put(headers[i].trim(), i);
            }

            validateColumns(columnIndex, lociColumns);

            String[] row;

            while ((row = reader.readNext()) != null) {

                String sampleId = getValue(row, columnIndex, "SampleID");
                String populationName = getValue(row, columnIndex, "Population");

                if (sampleId == null || sampleId.isBlank()) {
                    skippedCount++;
                    continue;
                }

                if (populationName == null || populationName.isBlank()) {
                    populationName = "Unknown";
                }

                Integer populationId = getOrCreatePopulation(populationName);
                UUID profileId = getOrCreateProfile(sampleId, populationId);

                profileCount++;

                for (String locus : lociColumns) {

                    String allelePair = getValue(row, columnIndex, locus);

                    if (allelePair == null || allelePair.isBlank() || allelePair.equalsIgnoreCase("NaN")) {
                        continue;
                    }

                    String[] alleles = allelePair.split(",");

                    if (alleles.length != 2) {
                        skippedCount++;
                        continue;
                    }

                    String allele1 = cleanAllele(alleles[0]);
                    String allele2 = cleanAllele(alleles[1]);

                    if (allele1.isBlank() || allele2.isBlank()) {
                        skippedCount++;
                        continue;
                    }

                    Integer locusId = getOrCreateLocus(locus);

                    boolean inserted = insertProfileGenotype(profileId, locusId, allele1, allele2);

                    if (inserted) {
                        genotypeCount++;
                    }
                }
            }
        }

        return "CSV import completed. Profiles processed: " + profileCount +
                ", Genotypes inserted: " + genotypeCount +
                ", Skipped values: " + skippedCount;
    }

    private void validateColumns(Map<String, Integer> columnIndex, String[] lociColumns) {

        if (!columnIndex.containsKey("SampleID")) {
            throw new RuntimeException("CSV missing required column: SampleID");
        }

        if (!columnIndex.containsKey("Population")) {
            throw new RuntimeException("CSV missing required column: Population");
        }

        for (String locus : lociColumns) {
            if (!columnIndex.containsKey(locus)) {
                throw new RuntimeException("CSV missing locus column: " + locus);
            }
        }
    }

    private String getValue(String[] row, Map<String, Integer> columnIndex, String columnName) {

        Integer index = columnIndex.get(columnName);

        if (index == null || index >= row.length) {
            return null;
        }

        return row[index] == null ? null : row[index].trim();
    }

    private String cleanAllele(String allele) {
        if (allele == null) {
            return "";
        }

        return allele.trim().replace("\"", "").replace("'", "");
    }

    private Integer getOrCreatePopulation(String name) {

        jdbcTemplate.update(
                "INSERT INTO populations(name) VALUES (?) ON CONFLICT (name) DO NOTHING",
                name
        );

        return jdbcTemplate.queryForObject(
                "SELECT id FROM populations WHERE name = ?",
                Integer.class,
                name
        );
    }

    private Integer getOrCreateLocus(String locus) {

        jdbcTemplate.update(
                "INSERT INTO str_loci(locus) VALUES (?) ON CONFLICT (locus) DO NOTHING",
                locus
        );

        return jdbcTemplate.queryForObject(
                "SELECT id FROM str_loci WHERE locus = ?",
                Integer.class,
                locus
        );
    }

    private UUID getOrCreateProfile(String sampleId, Integer populationId) {

        jdbcTemplate.update(
                """
                INSERT INTO profiles(sample_id, population_id)
                VALUES (?, ?)
                ON CONFLICT (sample_id) DO NOTHING
                """,
                sampleId,
                populationId
        );

        return jdbcTemplate.queryForObject(
                "SELECT id FROM profiles WHERE sample_id = ?",
                UUID.class,
                sampleId
        );
    }

    private boolean insertProfileGenotype(UUID profileId, Integer locusId, String allele1, String allele2) {

        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM profile_genotypes
                WHERE profile_id = ? AND locus_id = ?
                """,
                Integer.class,
                profileId,
                locusId
        );

        if (count != null && count > 0) {
            return false;
        }

        jdbcTemplate.update(
                """
                INSERT INTO profile_genotypes(profile_id, locus_id, allele1, allele2)
                VALUES (?, ?, ?, ?)
                """,
                profileId,
                locusId,
                allele1,
                allele2
        );

        return true;
    }
}