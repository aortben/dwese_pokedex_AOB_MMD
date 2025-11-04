package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Move;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Pokemon;

import java.sql.SQLException;
import java.util.List;

public interface PokemonDAO {
    //CRUD for Pokemon
    List<Pokemon> listAllPokemons() throws SQLException;
    void insertPokemon(Pokemon pokemon) throws SQLException;
    void updatePokemon(Pokemon pokemon) throws SQLException;
    void deletePokemon(Long id) throws SQLException;
    Pokemon getPokemonById(Long id);

    //Moves management
    List<Move> getMovesByPokemonId(Long pokemonId) throws SQLException;
    void addMoveToPokemon(Long pokemonId, Long moveId) throws SQLException;
    void removeMoveFromPokemon(Long pokemonId, Long moveId) throws SQLException;
    List<Move> listAllMoves() throws SQLException;

}
