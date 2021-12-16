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

CREATE TABLE category (
    id   SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE item_category (
    item_id     INT REFERENCES item (id),
    categories_id INT REFERENCES category (id),
    PRIMARY KEY (item_id, categories_id)
);

INSERT INTO category (name) VALUES ('Дом');
INSERT INTO category (name) VALUES ('Работа');
INSERT INTO category (name) VALUES ('Учеба');