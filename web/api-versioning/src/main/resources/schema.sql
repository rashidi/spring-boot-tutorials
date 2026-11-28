CREATE TABLE IF NOT EXISTS products (
    id       BIGSERIAL PRIMARY KEY,
    sku      VARCHAR(64)    NOT NULL UNIQUE,
    name     VARCHAR(150)   NOT NULL,
    category VARCHAR(80)    NOT NULL,
    price    NUMERIC(12, 2) NOT NULL,
    currency VARCHAR(3)     NOT NULL
);

CREATE TABLE IF NOT EXISTS inventory_items (
    id                BIGSERIAL PRIMARY KEY,
    sku               VARCHAR(64) NOT NULL UNIQUE,
    quantity          INTEGER     NOT NULL,
    reorder_threshold INTEGER     NOT NULL
);

CREATE TABLE IF NOT EXISTS catalog_audit_entries (
    id         BIGSERIAL PRIMARY KEY,
    sku        VARCHAR(64)              NOT NULL,
    action     VARCHAR(50)              NOT NULL,
    detail     VARCHAR(255)             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
