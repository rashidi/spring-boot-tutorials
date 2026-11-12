CREATE TABLE IF NOT EXISTS USERS(
                      `id` INT AUTO_INCREMENT PRIMARY KEY,
                      `name` TEXT NOT NULL,
                      `username` VARCHAR(60) NOT NULL UNIQUE
);