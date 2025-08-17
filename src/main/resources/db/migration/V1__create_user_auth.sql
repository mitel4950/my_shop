CREATE TABLE user_auth
(
    login     VARCHAR(50) PRIMARY KEY,
    password  VARCHAR(100) NOT NULL,
    nickname VARCHAR(40)  NOT NULL UNIQUE,
    role      VARCHAR(50)  NOT NULL
);
