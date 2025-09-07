CREATE TABLE IF NOT EXISTS goods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    description TEXT,
    price_per_unit NUMERIC(19,4) NOT NULL,
    stock_in_units BIGINT DEFAULT 0
);

