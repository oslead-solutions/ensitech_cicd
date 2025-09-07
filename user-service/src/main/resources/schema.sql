-- Ce fichier définit la structure de la base de données pour le user-service.

DROP TABLE IF EXISTS person CASCADE;

CREATE TABLE person (
    person_type VARCHAR(31) NOT NULL,
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    telephone VARCHAR(255),
    birthday DATE,
    gender VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    matricule VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT UK_person_email UNIQUE (email)
);