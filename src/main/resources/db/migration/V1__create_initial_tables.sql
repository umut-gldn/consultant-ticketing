-- ============================================================
-- COMPANIES - Firma Bilgileri
-- ============================================================

CREATE TABLE companies (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           type VARCHAR(50) NOT NULL CHECK (type IN ('consultant', 'client')),
                           email VARCHAR(255),
                           phone VARCHAR(50),
                           is_active BOOLEAN NOT NULL DEFAULT true,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- USERS - Kullanıcı Bilgileri
-- ============================================================

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       company_id UUID REFERENCES companies(id) ON DELETE SET NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       is_active BOOLEAN NOT NULL DEFAULT true,
                       email_verified BOOLEAN NOT NULL DEFAULT false,
                       failed_login_attempts INTEGER NOT NULL DEFAULT 0,
                       locked_until TIMESTAMPTZ,
                       last_login_at TIMESTAMPTZ,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- PROJECTS - Danısman-Musteri Projeleri
-- ============================================================

CREATE TABLE projects (
                          id UUID PRIMARY KEY,
                          consultant_company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
                          client_company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
                          name VARCHAR(255) NOT NULL,
                          slug VARCHAR(100) UNIQUE NOT NULL,
                          description TEXT,
                          is_active BOOLEAN NOT NULL DEFAULT true,
                          created_by UUID REFERENCES users(id) ON DELETE SET NULL,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- INDEXES
-- ============================================================

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_company_id ON users(company_id);

CREATE INDEX idx_projects_consultant ON projects(consultant_company_id);
CREATE INDEX idx_projects_client ON projects(client_company_id);
CREATE INDEX idx_projects_slug ON projects(slug);
