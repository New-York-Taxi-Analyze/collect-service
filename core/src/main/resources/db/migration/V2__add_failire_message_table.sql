CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS failure_message
(
    key        UUID PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    topic      VARCHAR(255)                NOT NULL,
    message    JSONB                       NOT NULL,
    status     VARCHAR(255)                NOT NULL default 'RETRY',
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX failure_message_status_idx ON failure_message (status);

CREATE OR REPLACE FUNCTION update_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER failure_message_updated_at
    BEFORE UPDATE
    ON failure_message
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();
