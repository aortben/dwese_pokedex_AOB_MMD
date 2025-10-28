package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.controllers;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao.PokemonDAO;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/pokemons")
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    @Autowired
    private PokemonDAO pokemonDAO;

    /**
     * Displays all pokemon.
     */
    @GetMapping
    public String listPokemons(Model model) {
        logger.info("Requesting the list of all pokemon...");
        try {
            List<Pokemon> pokemonList = pokemonDAO.listAllPokemons();
            model.addAttribute("pokemonList", pokemonList);
            logger.info("{} pokemon loaded successfully.", pokemonList.size());
        } catch (SQLException e) {
            logger.error("Error listing pokemon: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error listing pokemon.");
        }
        return "pokemon"; // Corresponds to pokemon.html
    }

    /**
     * Displays the form to create a new pokemon.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Showing form for new pokemon creation.");
        model.addAttribute("pokemon", new Pokemon());
        return "pokemon-form";
    }

    /**
     * Displays the form to edit an existing pokemon.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Showing edit form for pokemon ID {}", id);
        try {
            Pokemon pokemon = pokemonDAO.getPokemonById(id);
            if (pokemon == null) {
                logger.warn("Pokemon with ID {} not found.", id);
                model.addAttribute("errorMessage", "Pokemon not found.");
                return "redirect:/pokemons";
            }
            model.addAttribute("pokemon", pokemon);
        } catch (SQLException e) {
            logger.error("Error fetching pokemon with ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error fetching pokemon.");
        }
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
            pokemonDAO.insertPokemon(pokemon);
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
