package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.controllers;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao.PokemonDAO;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Pokemon;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pokemons")
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    @Autowired
    private PokemonDAO pokemonDAO;

    /**
     * Displays all pokemon with their moves.
     */
    @GetMapping
    public String listPokemons(Model model) {
        logger.info("Requesting the list of all pokemon...");
        try {
            List<Pokemon> pokemonList = pokemonDAO.listAllPokemons();

            // Cargar movimientos de cada Pokémon
            for (Pokemon p : pokemonList) {
                List<Move> moves = pokemonDAO.getMovesByPokemonId(p.getId());
                p.setMoves(moves);
                p.setMoveIds(moves.stream().map(Move::getId).toList());
            }

            model.addAttribute("pokemonList", pokemonList);
            logger.info("{} pokemon loaded successfully.", pokemonList.size());
        } catch (SQLException e) {
            logger.error("Error listing pokemon: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error listing pokemon.");
        }
        return "pokemon"; // Corresponds to pokemon.html
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("pokemon", new Pokemon());

        try {
            model.addAttribute("moves", pokemonDAO.listAllMoves());
        } catch (SQLException e) {
            model.addAttribute("moves", new ArrayList<>());
            model.addAttribute("errorMessage", "Error al cargar movimientos");
        }

        return "pokemon-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        Pokemon pokemon = pokemonDAO.getPokemonById(id);

        // Cargar movimientos del Pokémon
        try {
            List<Move> moves = pokemonDAO.getMovesByPokemonId(id);
            pokemon.setMoves(moves);
            pokemon.setMoveIds(moves.stream().map(Move::getId).toList());
            model.addAttribute("moves", pokemonDAO.listAllMoves());
        } catch (SQLException e) {
            model.addAttribute("moves", new ArrayList<>());
            model.addAttribute("errorMessage", "Error al cargar movimientos");
        }

        model.addAttribute("pokemon", pokemon);

        return "pokemon-form";
    }

    /**
     * Inserts a new pokemon into the database.
     */
    @PostMapping("/insert")
    public String insertPokemon(@ModelAttribute("pokemon") Pokemon pokemon,
                                RedirectAttributes redirectAttributes) {
        logger.info("Inserting new pokemon '{}'", pokemon.getName());
        try {
            // Inserta el Pokémon y obtiene su ID generado
            pokemonDAO.insertPokemon(pokemon);
            Long newPokemonId = pokemon.getId(); // Asegúrate que insertPokemon() actualice el objeto con el ID generado

            // Añadir movimientos seleccionados usando el ID del Pokémon insertado
            if (pokemon.getMoveIds() != null && newPokemonId != null) {
                for (Long moveId : pokemon.getMoveIds()) {
                    pokemonDAO.addMoveToPokemon(newPokemonId, moveId);
                }
            }

            logger.info("Pokemon '{}' inserted successfully.", pokemon.getName());
        } catch (SQLException e) {
            logger.error("Error inserting pokemon '{}': {}", pokemon.getName(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error inserting pokemon.");
        }
        return "redirect:/pokemons";
    }

    /**
     * Updates an existing pokemon.
     */
    @PostMapping("/update")
    public String updatePokemon(@ModelAttribute("pokemon") Pokemon pokemon,
                                RedirectAttributes redirectAttributes) {
        logger.info("Updating pokemon with ID {}", pokemon.getId());
        try {
            pokemonDAO.updatePokemon(pokemon);

            // Primero eliminamos todos los movimientos actuales
            List<Move> currentMoves = pokemonDAO.getMovesByPokemonId(pokemon.getId());
            for (Move m : currentMoves) {
                pokemonDAO.removeMoveFromPokemon(pokemon.getId(), m.getId());
            }

            // Añadimos los movimientos seleccionados
            if (pokemon.getMoveIds() != null) {
                for (Long moveId : pokemon.getMoveIds()) {
                    pokemonDAO.addMoveToPokemon(pokemon.getId(), moveId);
                }
            }

            logger.info("Pokemon with ID {} updated successfully.", pokemon.getId());
        } catch (SQLException e) {
            logger.error("Error updating pokemon with ID {}: {}", pokemon.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating pokemon.");
        }
        return "redirect:/pokemons";
    }

    /**
     * Deletes a pokemon by ID.
     */
    @PostMapping("/delete")
    public String deletePokemon(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting pokemon with ID {}", id);
        try {
            pokemonDAO.deletePokemon(id);
            logger.info("Pokemon with ID {} deleted successfully.", id);
        } catch (SQLException e) {
            logger.error("Error deleting pokemon with ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting pokemon.");
        }
        return "redirect:/pokemons";
    }
}
