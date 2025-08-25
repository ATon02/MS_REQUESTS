CREATE TABLE IF NOT EXISTS request_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    min_amount DOUBLE PRECISION NOT NULL,
    max_amount DOUBLE PRECISION NOT NULL,
    interest_rate DOUBLE PRECISION NOT NULL,
    automatic_validation BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS request_status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS request_client (
    id SERIAL PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    deadline BIGINT NOT NULL,
    email VARCHAR(150) NOT NULL,
    request_type_id INTEGER NOT NULL REFERENCES request_type(id),
    status_id INTEGER NOT NULL REFERENCES request_status(id),
    identity_document VARCHAR(50) NOT NULL
);