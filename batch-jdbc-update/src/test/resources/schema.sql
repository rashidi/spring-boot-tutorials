CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    status VARCHAR(50)
);

INSERT INTO users (id, name, username, status) VALUES (1, 'Leanne Graham', 'Bret', 'PENDING');
INSERT INTO users (id, name, username, status) VALUES (2, 'Ervin Howell', 'Antonette', 'PENDING');
INSERT INTO users (id, name, username, status) VALUES (3, 'Clementine Bauch', 'Samantha', 'PENDING');
