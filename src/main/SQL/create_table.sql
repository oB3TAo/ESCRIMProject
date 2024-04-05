USE escrim;

CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(50) NOT NULL
);

CREATE TABLE personnel (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(50) NOT NULL,
                           role VARCHAR(50) NOT NULL,
                           items VARCHAR(50) NOT NULL
);