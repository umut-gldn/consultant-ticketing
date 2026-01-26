-- ============================================================
-- COMPANY ROLES (scope: company)
-- ============================================================

INSERT INTO roles (id,name, display_name, description, scope, is_system_role) VALUES
                                                                               (gen_random_uuid(),'owner', 'Firma Sahibi', 'Firma uzerinde tam yetki', 'COMPANY', true),
                                                                               (gen_random_uuid(),'admin', 'Firma Yöneticisi', 'Firma yönetim yetkileri', 'COMPANY', true),
                                                                               (gen_random_uuid(),'member', 'Firma Üyesi', 'Temel firma üyeliği', 'COMPANY', true);

-- ============================================================
-- PROJECT ROLES (scope: project)
-- ============================================================

INSERT INTO roles (id, name, display_name, description, scope, is_system_role) VALUES
                                                                               (gen_random_uuid(),'customer_viewer', 'Sadece Görüntüleyici', 'Sadece ticketları ve yorumları görüntüleyebilir', 'PROJECT', true),
                                                                               (gen_random_uuid(),'customer_standard', 'Standart Kullanıcı', 'Ticket açabilir, yorum yapabilir', 'PROJECT', true),
                                                                               (gen_random_uuid(),'customer_advanced', 'Gelişmiş Kullanıcı', 'Ticket açabilir, güncelleyebilir, kapatabilir', 'PROJECT', true),
                                                                               (gen_random_uuid(),'project_admin', 'Proje Yöneticisi', 'Proje içinde tüm yetkilere sahip', 'PROJECT', true);

-- ============================================================
-- PERMISSIONS
-- ============================================================

INSERT INTO permissions (id, name, resource, action, description) VALUES
-- Ticket
(gen_random_uuid(),'ticket.create', 'ticket', 'create', 'Ticket oluşturabilir'),
(gen_random_uuid(),'ticket.read', 'ticket', 'read', 'Ticketları görüntüleyebilir'),
(gen_random_uuid(),'ticket.update', 'ticket', 'update', 'Ticket güncelleyebilir'),
(gen_random_uuid(),'ticket.delete', 'ticket', 'delete', 'Ticket silebilir'),
(gen_random_uuid(),'ticket.close', 'ticket', 'close', 'Ticket kapatabilir'),
(gen_random_uuid(),'ticket.reopen', 'ticket', 'reopen', 'Kapalı ticketı tekrar açabilir'),
(gen_random_uuid(),'ticket.assign', 'ticket', 'assign', 'Ticket atayabilir'),
-- Comment
(gen_random_uuid(),'comment.create', 'comment', 'create', 'Yorum yapabilir'),
(gen_random_uuid(),'comment.read', 'comment', 'read', 'Yorumları okuyabilir'),
(gen_random_uuid(),'comment.update', 'comment', 'update', 'Kendi yorumunu düzenleyebilir'),
(gen_random_uuid(),'comment.delete', 'comment', 'delete', 'Kendi yorumunu silebilir'),
-- Attachment
(gen_random_uuid(),'attachment.upload', 'attachment', 'upload', 'Dosya yükleyebilir'),
(gen_random_uuid(),'attachment.download', 'attachment', 'download', 'Dosya indirebilir'),
(gen_random_uuid(),'attachment.delete', 'attachment', 'delete', 'Dosya silebilir'),
-- User
(gen_random_uuid(),'user.invite', 'user', 'invite', 'Kullanıcı davet edebilir'),
(gen_random_uuid(),'user.manage', 'user', 'manage', 'Kullanıcı yetkilerini yönetebilir'),
(gen_random_uuid(),'user.remove', 'user', 'remove', 'Kullanıcı çıkarabilir'),
-- Project
(gen_random_uuid(),'project.read', 'project', 'read', 'Proje görüntüleyebilir'),
(gen_random_uuid(),'project.update', 'project', 'update', 'Proje güncelleyebilir'),
(gen_random_uuid(),'project.settings', 'project', 'settings', 'Proje ayarlarını değiştirebilir');

-- ============================================================
-- ROLE-PERMISSION MAPPINGS (Sadece Project Rolleri İçin)
-- ============================================================

-- CUSTOMER_VIEWER: Sadece okuma
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'customer_viewer' AND r.scope = 'project'
  AND p.name IN ('ticket.read', 'comment.read', 'attachment.download', 'project.read');

-- CUSTOMER_STANDARD: Okuma + Ticket/Yorum olusturma
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'customer_standard' AND r.scope = 'project'
  AND p.name IN (
                 'ticket.create', 'ticket.read', 'ticket.update',
                 'comment.create', 'comment.read',
                 'attachment.upload', 'attachment.download',
                 'project.read'
    );

-- CUSTOMER_ADVANCED: Standard + Kapatma
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'customer_advanced' AND r.scope = 'project'
  AND p.name IN (
                 'ticket.create', 'ticket.read', 'ticket.update', 'ticket.close',
                 'comment.create', 'comment.read', 'comment.update',
                 'attachment.upload', 'attachment.download', 'attachment.delete',
                 'project.read'
    );

-- PROJECT_ADMIN: Tüm proje yetkileri
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'project_admin' AND r.scope = 'project'
  AND (p.resource IN ('ticket', 'comment', 'attachment', 'project') OR p.name = 'user.invite');