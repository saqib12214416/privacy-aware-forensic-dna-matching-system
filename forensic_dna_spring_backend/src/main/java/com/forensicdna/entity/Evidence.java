package com.forensicdna.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "evidence")
public class Evidence {

    @Id
    private UUID id;

    @Column(name = "evidence_code")
    private String evidenceCode;

    @Column(name = "submitted_by")
    private UUID submittedBy;

    @Column(name = "received_at")
    private OffsetDateTime receivedAt;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEvidenceCode() { return evidenceCode; }
    public void setEvidenceCode(String evidenceCode) { this.evidenceCode = evidenceCode; }

    public UUID getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(UUID submittedBy) { this.submittedBy = submittedBy; }

    public OffsetDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(OffsetDateTime receivedAt) { this.receivedAt = receivedAt; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}