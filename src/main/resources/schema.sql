-- Crear tabla regiones
CREATE TABLE IF NOT EXISTS region (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
    );

-- Crear tabla rutas
CREATE TABLE IF NOT EXISTS route (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
    );

-- Crear tabla pokemons
CREATE TABLE IF NOT EXISTS pokemon(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    level INT NOT NULL
    );

-- Crear tabla movimientos
CREATE TABLE IF NOT EXISTS move (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    power INT NOT NULL,
    accuracy INT NOT NULL,
    PP INT NOT NULL
    );

-- Tabla entre pokemon y movimientos para gestionar las limitaciones (Por ejemplo el maximo de movimientos de un pokemon es 4)
CREATE TABLE pokemon_moves (
    pokemon_id BIGINT NOT NULL,
    move_id BIGINT NOT NULL,
    PRIMARY KEY (pokemon_id, move_id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemons(id),
    FOREIGN KEY (move_id) REFERENCES moves(id)
    );