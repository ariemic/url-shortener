-- URL Shortener Database Schema

CREATE TABLE IF NOT EXISTS url_mappings (
    short_code VARCHAR(20) PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Future: Add click_count, expires_at, user_id, etc.
    CONSTRAINT unique_short_code UNIQUE (short_code)
);

-- Index for fast lookup by original URL (for deduplication)
CREATE INDEX IF NOT EXISTS idx_original_url ON url_mappings(original_url);

-- Future: Add analytics table
-- CREATE TABLE url_analytics (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     short_code VARCHAR(20) NOT NULL,
--     accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     user_agent VARCHAR(255),
--     ip_address VARCHAR(45),
--     FOREIGN KEY (short_code) REFERENCES url_mappings(short_code)
-- );
