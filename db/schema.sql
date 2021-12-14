CREATE TABLE users (
    id       SERIAL PRIMARY KEY,
    name     TEXT,
    email    TEXT NOT NULL UNIQUE,
    password TEXT,
    created  TIMESTAMP DEFAULT now(),
    updated  TIMESTAMP DEFAULT now()
);

CREATE TABLE item (
    id          SERIAL PRIMARY KEY,
    description TEXT,
    done        BOOLEAN   DEFAULT FALSE,
    user_id     INT REFERENCES users (id),
    created     TIMESTAMP DEFAULT now(),
    updated     TIMESTAMP DEFAULT now()
);