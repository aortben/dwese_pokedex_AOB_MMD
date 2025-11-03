package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Move;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Implementation of MoveDAO using Spring JdbcTemplate.
 * Handles all database operations for Move entity.
 */
@Repository
public class MoveDAOImpl implements MoveDAO {

    private static final Logger logger = LoggerFactory.getLogger(MoveDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // JdbcTemplate injection via constructor
    public MoveDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * List all moves in the database.
     */
    @Override
    public List<Move> listAllMoves() {
        logger.info("Listing all moves from the database.");
        String sql = "SELECT * FROM move";
        List<Move> moves = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Move.class));
        logger.info("Retrieved {} moves from the database.", moves.size());
        return moves;
    }

    /**
     * Insert a new move into the database.
     */
    @Override
    public void insertMove(Move move) {
        logger.info("Inserting move with name: {}", move.getName());
        String sql = "INSERT INTO move (name, type, category, power, accuracy, pp) VALUES (?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, move.getName(), move.getType(),
                move.getCategory().name(), move.getPower(), move.getAccuracy(), move.getPp());
        logger.info("Inserted move. Rows affected: {}", rowsAffected);
    }

    /**
     * Update an existing move.
     */
    @Override
    public void updateMove(Move move) {
        logger.info("Updating move with id: {}", move.getId());
        String sql = "UPDATE move SET name = ?, type = ?, category = ?, power = ?, accuracy = ?, pp = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, move.getName(), move.getType(), move.getCategory().name(),
                move.getPower(), move.getAccuracy(), move.getPp(), move.getId());
        logger.info("Updated move. Rows affected: {}", rowsAffected);
    }

    /**
     * Delete a move by its ID.
     */
    @Override
    public void deleteMove(Long id) {
        logger.info("Deleting move with id: {}", id);
        String sql = "DELETE FROM move WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted move. Rows affected: {}", rowsAffected);
    }

    /**
     * Retrieve a move by its ID.
     */
    @Override
    public Move getMoveById(Long id) {
        logger.info("Retrieving move by id: {}", id);
        String sql = "SELECT * FROM move WHERE id = ?";
        try {
            Move move = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Move.class), id);
            logger.info("Move retrieved: {}", move.getName());
            return move;
        } catch (Exception e) {
            logger.warn("No move found with id: {}", id);
            return null;
        }
    }

    /**
     * Check if a move exists by its name.
     */
    @Override
    public boolean existsMoveByName(String name) {
        logger.info("Checking if move with name: {} exists", name);
        String sql = "SELECT COUNT(*) FROM move WHERE UPPER(name) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.toUpperCase());
        boolean exists = count != null && count > 0;
        logger.info("Move with name '{}' exists: {}", name, exists);
        return exists;
    }

    /**
     * Check if a move exists by name excluding a specific ID.
     */
    @Override
    public boolean existsMoveByNameAndNotId(String name, Long id) {
        logger.info("Checking if move with name: {} exists excluding id: {}", name, id);
        String sql = "SELECT COUNT(*) FROM move WHERE UPPER(name) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.toUpperCase(), id);
        boolean exists = count != null && count > 0;
        logger.info("Move with name '{}' exists excluding id {}: {}", name, id, exists);
        return exists;
    }
}

