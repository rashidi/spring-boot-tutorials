CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_email VARCHAR(255) NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL
);