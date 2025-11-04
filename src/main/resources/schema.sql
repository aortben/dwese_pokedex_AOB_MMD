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
CREATE TABLE IF NOT EXISTS pokemon (
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

-- Tabla intermedia entre pokemon y movimientos
CREATE TABLE IF NOT EXISTS pokemon_moves (
    pokemon_id INT NOT NULL,
    move_id INT NOT NULL,
    PRIMARY KEY (pokemon_id, move_id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemon(id),
    FOREIGN KEY (move_id) REFERENCES move(id)
);

CREATE TABLE IF NOT EXISTS route_pokemon (
    route_id INT NOT NULL,
    pokemon_id INT NOT NULL,
    PRIMARY KEY (route_id, pokemon_id),
    FOREIGN KEY (route_id) REFERENCES route(id) ON DELETE CASCADE,
    FOREIGN KEY (pokemon_id) REFERENCES pokemon(id) ON DELETE CASCADE
);


