CREATE TABLE IF NOT EXISTS history (
    id SERIAL PRIMARY KEY,
    who VARCHAR(255),
    when_requested TIMESTAMP,
    what TEXT
);