package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import java.sql.SQLException;
import java.util.List;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Move;

/**
 * DAO interface for Move entity.
 * Defines all database operations related to Move objects.
 */
public interface MoveDAO {
    // List all moves
    List<Move> listAllMoves() throws SQLException;
    // Insert a new move
    void insertMove(Move move) throws SQLException;
    // Update an existing move
    void updateMove(Move move) throws SQLException;
    // Delete a move by its ID
    void deleteMove(Long id) throws SQLException;
    // Retrieve a move by its ID
    Move getMoveById(Long id) throws SQLException;
    // Check if a move exists by name
    boolean existsMoveByName(String name) throws SQLException;
    // Check if a move exists by name excluding a specific ID (useful for updates)
    boolean existsMoveByNameAndNotId(String name, Long id) throws SQLException;
}
