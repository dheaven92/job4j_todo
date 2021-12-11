CREATE TABLE item (
    id          SERIAL PRIMARY KEY,
    description TEXT,
    done        BOOLEAN   DEFAULT FALSE,
    created     TIMESTAMP DEFAULT now(),
    updated     TIMESTAMP DEFAULT now()
);