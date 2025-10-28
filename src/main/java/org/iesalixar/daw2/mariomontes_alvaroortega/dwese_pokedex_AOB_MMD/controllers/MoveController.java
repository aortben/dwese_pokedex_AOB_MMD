package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.controllers;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao.MoveDAO;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

public class MoveController {

    private static final Logger logger = LoggerFactory.getLogger(MoveController.class);

    @Autowired
    private MoveDAO moveDAO;

    /**
     * Displays all moves.
     */
    @GetMapping
    public String listMoves(Model model) {
        logger.info("Requesting the list of all moves...");
        try {
            List<Move> moveList = moveDAO.listAllMoves();
            model.addAttribute("moveList", moveList);
            logger.info("{} moves loaded successfully.", moveList.size());
        } catch (SQLException e) {
            logger.error("Error listing moves: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error listing moves.");
        }
        return "move"; // Corresponds to move.html
    }

    /**
     * Displays the form to create a new move.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Showing form for new move creation.");
        model.addAttribute("move", new Move());
        return "move-form";
    }

    /**
     * Displays the form to edit an existing move.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Showing edit form for move ID {}", id);
        try {
            Move move = moveDAO.getMoveById(id);
            if (move == null) {
                logger.warn("Move with ID {} not found.", id);
                model.addAttribute("errorMessage", "Move not found.");
                return "redirect:/moves";
            }
            model.addAttribute("move", move);
        } catch (SQLException e) {
            logger.error("Error fetching move with ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error fetching move.");
        }
        return "move-form";
    }

    /**
     * Inserts a new move into the database.
     */
    @PostMapping("/insert")
    public String insertMove(@ModelAttribute("move") Move move,
                             RedirectAttributes redirectAttributes) {
        logger.info("Inserting new move '{}'", move.getName());
        try {
            if (moveDAO.existsMoveByName(move.getName())) {
                logger.warn("Move with name '{}' already exists.", move.getName());
                redirectAttributes.addFlashAttribute("errorMessage", "A move with that name already exists.");
                return "redirect:/moves/new";
            }
            moveDAO.insertMove(move);
            logger.info("Move '{}' inserted successfully.", move.getName());
        } catch (SQLException e) {
            logger.error("Error inserting move '{}': {}", move.getName(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error inserting move.");
        }
        return "redirect:/moves";
    }

    /**
     * Updates an existing move.
     */
    @PostMapping("/update")
    public String updateMove(@ModelAttribute("move") Move move,
                             RedirectAttributes redirectAttributes) {
        logger.info("Updating move with ID {}", move.getId());
        try {
            if (moveDAO.existsMoveByNameAndNotId(move.getName(), move.getId())) {
                logger.warn("Move name '{}' already used by another move.", move.getName());
                redirectAttributes.addFlashAttribute("errorMessage", "That move name already exists.");
                return "redirect:/moves/edit?id=" + move.getId();
            }
            moveDAO.updateMove(move);
            logger.info("Move with ID {} updated successfully.", move.getId());
        } catch (SQLException e) {
            logger.error("Error updating move with ID {}: {}", move.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating move.");
        }
        return "redirect:/moves";
    }

    /**
     * Deletes a move by ID.
     */
    @PostMapping("/delete")
    public String deleteMove(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting move with ID {}", id);
        try {
            moveDAO.deleteMove(id);
            logger.info("Move with ID {} deleted successfully.", id);
        } catch (SQLException e) {
            logger.error("Error deleting move with ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting move.");
        }
        return "redirect:/moves";
    }
}
