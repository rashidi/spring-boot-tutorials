CREATE TABLE IF NOT EXISTS one_time_tokens (
    token_value VARCHAR(36) NOT NULL PRIMARY KEY,
    username    VARCHAR(50) NOT NULL,
    expires_at  TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    id           BIGSERIAL PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    email        VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    role         VARCHAR(30)  NOT NULL,
    enabled      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS token_deliveries (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50)              NOT NULL,
    recipient_email VARCHAR(255)             NOT NULL,
    token_value     VARCHAR(64)              NOT NULL,
    magic_link      VARCHAR(512)             NOT NULL,
    expires_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    delivered_at    TIMESTAMP WITH TIME ZONE NOT NULL
);
