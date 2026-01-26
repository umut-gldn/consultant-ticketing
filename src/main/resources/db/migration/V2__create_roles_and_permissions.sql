-- ============================================================
-- ROLES - Rol Tanimlari
-- ============================================================
-- scope: 'company' veya 'project'
-- Ayni tabloda hem firma hem proje rolleri tutulur
-- ============================================================

CREATE TABLE roles (
                       id UUID PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       display_name VARCHAR(255) NOT NULL,
                       description TEXT,
                       scope VARCHAR(50) NOT NULL CHECK (scope IN ('COMPANY', 'PROJECT')),
                       is_system_role BOOLEAN NOT NULL DEFAULT true,
                       created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                       UNIQUE(name, scope)
);

-- ============================================================
-- PERMISSIONS - Yetki Tanimlari
-- ============================================================

CREATE TABLE permissions (
                             id UUID PRIMARY KEY,
                             name VARCHAR(100) UNIQUE NOT NULL,
                             resource VARCHAR(50) NOT NULL,
                             action VARCHAR(50) NOT NULL,
                             description TEXT,
                             created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- ROLE_PERMISSIONS - Rol-Yetki İliskisi
-- ============================================================

CREATE TABLE role_permissions (
                                  role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                                  permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
                                  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (role_id, permission_id)
);

-- ============================================================
-- COMPANY_USERS - Firma Seviyesi Roller
-- ============================================================
-- role_id ile roles tablosuna referans (scope: company)
-- ============================================================

CREATE TABLE company_users (
                               company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
                               user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               role_id UUID NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
                               created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (company_id, user_id)
);

-- ============================================================
-- PROJECT_USERS - Proje Seviyesi Roller
-- ============================================================
-- role_id ile roles tablosuna referans (scope: project)
-- ============================================================

CREATE TABLE project_users (
                               id UUID PRIMARY KEY,
                               project_id UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
                               user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               role_id UUID NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
                               invited_by UUID REFERENCES users(id) ON DELETE SET NULL,
                               invited_at TIMESTAMPTZ,
                               joined_at TIMESTAMPTZ,
                               is_active BOOLEAN DEFAULT true,
                               created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                               UNIQUE(project_id, user_id)
);

-- ============================================================
-- PROJECT_USER_PERMISSIONS - Custom Yetkiler (Override)
-- ============================================================

CREATE TABLE project_user_permissions (
                                          id UUID PRIMARY KEY,
                                          project_user_id UUID NOT NULL REFERENCES project_users(id) ON DELETE CASCADE,
                                          permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
                                          is_granted BOOLEAN DEFAULT true,
                                          granted_by UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                          created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                                          UNIQUE(project_user_id, permission_id)
);

-- ============================================================
-- INVITATIONS - Email Davetleri
-- ============================================================

CREATE TABLE invitations (
                             id UUID PRIMARY KEY,
                             project_id UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
                             email VARCHAR(255) NOT NULL,
                             role_id UUID NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
                             token VARCHAR(255) UNIQUE NOT NULL,
                             invited_by UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             expires_at TIMESTAMPTZ NOT NULL,
                             accepted_at TIMESTAMPTZ,
                             created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- INDEXES
-- ============================================================

CREATE INDEX idx_roles_scope ON roles(scope);

CREATE INDEX idx_role_permissions_role ON role_permissions(role_id);
CREATE INDEX idx_role_permissions_permission ON role_permissions(permission_id);

CREATE INDEX idx_company_users_company ON company_users(company_id);
CREATE INDEX idx_company_users_user ON company_users(user_id);
CREATE INDEX idx_company_users_role ON company_users(role_id);

CREATE INDEX idx_project_users_project ON project_users(project_id);
CREATE INDEX idx_project_users_user ON project_users(user_id);
CREATE INDEX idx_project_users_role ON project_users(role_id);

CREATE INDEX idx_project_user_permissions_project_user ON project_user_permissions(project_user_id);

CREATE INDEX idx_invitations_token ON invitations(token);
CREATE INDEX idx_invitations_email ON invitations(email);
CREATE INDEX idx_invitations_project ON invitations(project_id);

