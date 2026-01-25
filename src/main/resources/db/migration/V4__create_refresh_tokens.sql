-- ============================================================
-- REFRESH_TOKENS - JWT Refresh Token Yönetimi
-- ============================================================
-- NEDEN DATABASE'DE?
-- - Logout yapabilmek için (token iptal)
-- - Cihaz yönetimi (hangi cihazlar aktif)
-- - Güvenlik (şüpheli aktivite tespiti)
-- - Token rotation (her refresh'te yeni token)
--
-- TOKEN HAKKINDA:
-- - token alanı SHA-256 hash saklar, plain token ASLA saklanmaz
-- - Kullanıcıya plain token döndürülür
-- - Doğrulama: gelen token hash'lenir, DB'deki ile karşılaştırılır
-- ============================================================

CREATE TABLE refresh_tokens (
                                id UUID PRIMARY KEY,
                                user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                token VARCHAR(255) UNIQUE NOT NULL,             -- SHA-256 hash stored, NOT plain token
                                expires_at TIMESTAMPTZ NOT NULL,
                                created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                                revoked_at TIMESTAMPTZ,
                                device_info VARCHAR(255),
                                ip_address VARCHAR(50)
);

-- ============================================================
-- INDEXES
-- ============================================================

-- Token lookup icin (login/refresh)
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);

-- Kullanicinin tüm token'larini bulmak icin (logout all, cihaz listesi)
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);

-- Sadece aktif (revoke edilmemis) token'lar icin unique constraint
CREATE UNIQUE INDEX idx_refresh_tokens_active ON refresh_tokens(token) WHERE revoked_at IS NULL;

-- Kullanıcının aktif ve gecerli token'larını bulmak için
CREATE INDEX idx_refresh_tokens_valid ON refresh_tokens(user_id, expires_at) WHERE revoked_at IS NULL;