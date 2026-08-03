CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS roles (
    id INTEGER PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

INSERT INTO roles (id, code, description) VALUES
(1, 'admin', 'System administrator'),
(2, 'investigator', 'Investigator'),
(3, 'field', 'Field officer')
ON CONFLICT (id) DO NOTHING;

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role_id INTEGER NOT NULL REFERENCES roles(id),
    password_hash TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

INSERT INTO users (email, full_name, role_id, password_hash, is_active)
VALUES
('admin', 'System Admin', 1, 'admin', TRUE),
('investigator', 'Investigator User', 2, 'admin', TRUE),
('field', 'Field Officer', 3, 'admin', TRUE)
ON CONFLICT (email) DO NOTHING;

CREATE TABLE IF NOT EXISTS populations (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

INSERT INTO populations (name) VALUES
('General Population')
ON CONFLICT (name) DO NOTHING;

CREATE TABLE IF NOT EXISTS str_loci (
    id SERIAL PRIMARY KEY,
    locus TEXT UNIQUE NOT NULL
);

INSERT INTO str_loci (id, locus) VALUES
(1, 'FGA'),
(2, 'TPO'),
(3, 'D1S1609'),
(4, 'D2S441'),
(5, 'D8S1108'),
(6, 'D21S2052'),
(7, 'D18S51')
ON CONFLICT (id) DO NOTHING;

CREATE TABLE IF NOT EXISTS profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sample_id TEXT UNIQUE,
    population_id INTEGER REFERENCES populations(id),
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

CREATE TABLE IF NOT EXISTS profile_genotypes (
    id SERIAL PRIMARY KEY,
    profile_id UUID REFERENCES profiles(id) ON DELETE CASCADE,
    locus_id INTEGER REFERENCES str_loci(id),
    allele1 VARCHAR(50),
    allele2 VARCHAR(50),
    CONSTRAINT uq_profile_locus UNIQUE (profile_id, locus_id)
);

CREATE TABLE IF NOT EXISTS profile_privacy_encodings (
    id SERIAL PRIMARY KEY,
    profile_id UUID REFERENCES profiles(id) ON DELETE CASCADE,
    locus_id INTEGER REFERENCES str_loci(id),
    encoded_allele1 TEXT,
    encoded_allele2 TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_profile_privacy_locus UNIQUE (profile_id, locus_id)
);

CREATE TABLE IF NOT EXISTS allele_frequencies (
    id SERIAL PRIMARY KEY,
    population_id INTEGER REFERENCES populations(id),
    locus_id INTEGER REFERENCES str_loci(id),
    allele VARCHAR(50),
    frequency NUMERIC
);

CREATE TABLE IF NOT EXISTS evidence (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    evidence_code TEXT UNIQUE NOT NULL,
    submitted_by UUID REFERENCES users(id),
    received_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB
);

CREATE TABLE IF NOT EXISTS evidence_genotypes (
    id SERIAL PRIMARY KEY,
    evidence_id UUID REFERENCES evidence(id) ON DELETE CASCADE,
    locus_id INTEGER REFERENCES str_loci(id),
    allele1 VARCHAR(50),
    allele2 VARCHAR(50),
    CONSTRAINT uq_evidence_locus UNIQUE (evidence_id, locus_id)
);

CREATE TABLE IF NOT EXISTS evidence_matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    evidence_id UUID REFERENCES evidence(id) ON DELETE CASCADE,
    profile_id UUID REFERENCES profiles(id) ON DELETE CASCADE,
    score NUMERIC,
    matched_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS feedback (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    module VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    file_path TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    action TEXT NOT NULL,
    details JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_profiles_sample_id ON profiles(sample_id);
CREATE INDEX IF NOT EXISTS idx_profiles_population_id ON profiles(population_id);
CREATE INDEX IF NOT EXISTS idx_profile_genotypes_profile_id ON profile_genotypes(profile_id);
CREATE INDEX IF NOT EXISTS idx_profile_genotypes_locus_id ON profile_genotypes(locus_id);
CREATE INDEX IF NOT EXISTS idx_evidence_code ON evidence(evidence_code);
CREATE INDEX IF NOT EXISTS idx_evidence_genotypes_evidence_id ON evidence_genotypes(evidence_id);
CREATE INDEX IF NOT EXISTS idx_evidence_matches_evidence_id ON evidence_matches(evidence_id);
CREATE INDEX IF NOT EXISTS idx_evidence_matches_profile_id ON evidence_matches(profile_id);
CREATE INDEX IF NOT EXISTS idx_feedback_user_id ON feedback(user_id);
CREATE INDEX IF NOT EXISTS idx_feedback_module ON feedback(module);
CREATE INDEX IF NOT EXISTS idx_feedback_status ON feedback(status);