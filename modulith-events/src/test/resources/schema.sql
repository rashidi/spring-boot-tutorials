CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    customer_email VARCHAR(255) NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL
);