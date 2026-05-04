-- PetManager Database Schema
-- MySQL 8.x

CREATE DATABASE IF NOT EXISTS petmanager;
USE petmanager;

-- Tabla para Veterinarias (debe crearse antes que persons por el FK)
CREATE TABLE IF NOT EXISTS vets (
    id_vet BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone INT NOT NULL,
    street VARCHAR(200),
    city VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla base para Owner y Donor usando discriminador de tipo
-- preferred_vet_id: asociación simple Owner → Vet (muchos-a-uno, unidireccional)
CREATE TABLE IF NOT EXISTS persons (
    dni INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    phone BIGINT NOT NULL,
    street VARCHAR(200),
    city VARCHAR(100),
    type ENUM('OWNER', 'DONOR') NOT NULL,
    preferred_vet_id BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (preferred_vet_id) REFERENCES vets(id_vet) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla para Pets (Dog y Cat) usando discriminador de tipo
CREATE TABLE IF NOT EXISTS pets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    race VARCHAR(100),
    is_sick BOOLEAN DEFAULT FALSE,
    description TEXT,
    type ENUM('DOG', 'CAT') NOT NULL,
    status ENUM('ACTIVA', 'PERDIDA', 'FALLECIDA') DEFAULT 'ACTIVA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Relación muchos-a-muchos entre Owner y Pet
CREATE TABLE IF NOT EXISTS owner_pet (
    owner_dni INT NOT NULL,
    pet_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (owner_dni, pet_id),
    FOREIGN KEY (owner_dni) REFERENCES persons(dni) ON DELETE CASCADE,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para optimización
CREATE INDEX idx_persons_type ON persons(type);
CREATE INDEX idx_pets_type ON pets(type);
CREATE INDEX idx_persons_dni ON persons(dni);
CREATE INDEX idx_pets_id ON pets(id);
