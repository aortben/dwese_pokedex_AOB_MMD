package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Pokemon;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of the PokemonDAO interface.
 * Handles database operations related to Pokemon entities and their associated Moves.
 */
@Repository
public class PokemonDAOImpl implements PokemonDAO {

    private static final Logger logger = LoggerFactory.getLogger(PokemonDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Constructor injection of JdbcTemplate
    public PokemonDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Pokemon> listAllPokemons() throws SQLException {
        logger.info("Listing all Pokémon from the database.");
        String sql = "SELECT * FROM pokemon";
        List<Pokemon> pokemons = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Pokemon.class));
        logger.info("Retrieved {} Pokémon from the database.", pokemons.size());
        return pokemons;
    }

    @Override
    public void insertPokemon(Pokemon pokemon) throws SQLException {
        logger.info("Inserting Pokémon: {}", pokemon.getName());
        String sql = "INSERT INTO pokemon (name, type, level) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, pokemon.getName(), pokemon.getType(), pokemon.getLevel());
        logger.info("Inserted Pokémon '{}'. Rows affected: {}", pokemon.getName(), rowsAffected);
    }

    @Override
    public void updatePokemon(Pokemon pokemon) throws SQLException {
        logger.info("Updating Pokémon with ID {}", pokemon.getId());
        String sql = "UPDATE pokemon SET name = ?, type = ?, level = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, pokemon.getName(), pokemon.getType(), pokemon.getLevel(), pokemon.getId());
        logger.info("Updated Pokémon with ID {}. Rows affected: {}", pokemon.getId(), rowsAffected);
    }

    @Override
    public void deletePokemon(Long id) throws SQLException {
        logger.info("Deleting Pokémon with ID {}", id);
        String sql = "DELETE FROM pokemon WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted Pokémon with ID {}. Rows affected: {}", id, rowsAffected);
    }

    @Override
    public Pokemon getPokemonById(Long id) {
        logger.info("Fetching Pokémon by ID {}", id);
        String sql = "SELECT * FROM pokemon WHERE id = ?";
        try {
            Pokemon pokemon = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Pokemon.class), id);
            logger.info("Pokémon retrieved: {} (Type: {}, Level: {})", pokemon.getName(), pokemon.getType(), pokemon.getLevel());
            return pokemon;
        } catch (Exception e) {
            logger.warn("No Pokémon found with ID {}", id);
            return null;
        }
    }

    // ========================= MOVES SECTION =========================

    @Override
    public List<Move> getMovesByPokemonId(Long pokemonId) throws SQLException {
        logger.info("Listing moves for Pokémon ID {}", pokemonId);
        String sql = """
                SELECT m.* 
                FROM move m                
                JOIN pokemon_moves pm ON m.id = pm.move_id
                WHERE pm.pokemon_id = ?
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Move.class), pokemonId);
    }

    @Override
    public void addMoveToPokemon(Long pokemonId, Long moveId) throws SQLException {
        List<Move> currentMoves = getMovesByPokemonId(pokemonId);

        if (currentMoves.size() >= 4) {
            logger.warn("Cannot add move: Pokémon ID {} already has 4 moves.", pokemonId);
            throw new IllegalStateException("A Pokémon can only have 4 moves.");
        }

        logger.info("Adding move ID {} to Pokémon ID {}", moveId, pokemonId);
        String sql = "INSERT INTO pokemon_moves (pokemon_id, move_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, pokemonId, moveId);
    }

    @Override
    public void removeMoveFromPokemon(Long pokemonId, Long moveId) throws SQLException {
        logger.info("Removing move ID {} from Pokémon ID {}", moveId, pokemonId);
        String sql = "DELETE FROM pokemon_moves WHERE pokemon_id = ? AND move_id = ?";
        jdbcTemplate.update(sql, pokemonId, moveId);
    }
}


