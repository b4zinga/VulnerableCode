DROP TABLE IF EXISTS "users";
CREATE TABLE users (
    `id` INTEGER PRIMARY KEY,
    `username` TEXT,
    `password` TEXT
);

INSERT INTO users VALUES (1, "tom", "123456");
INSERT INTO users VALUES (2, "myc", "qazwsx");
