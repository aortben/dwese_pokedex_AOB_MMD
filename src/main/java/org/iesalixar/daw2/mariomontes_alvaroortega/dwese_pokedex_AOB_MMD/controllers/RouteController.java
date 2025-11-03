package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.controllers;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao.PokemonDAO;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao.RouteDAO;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Pokemon;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private static final Logger logger =
            LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private RouteDAO routeDAO;

    @Autowired
    private PokemonDAO pokemonDAO;
    // Listado de rutas
    @GetMapping
    public String listRoutes(Model model) {
        logger.info("Solicitando la lista de todas las rutas...");
        List<Route> listRoutes = new ArrayList<>();
        try {
            listRoutes = routeDAO.listAllRoutes();
            logger.info("Se han cargado {} rutas.", listRoutes.size());
        } catch (SQLException e) {
            logger.error("Error al listar las rutas: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las rutas.");
        }
        model.addAttribute("routeList", listRoutes);
        return "route";
    }

    // Formulario para nueva ruta
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva ruta.");
        model.addAttribute("route", new Route());
        try {
            model.addAttribute("allPokemons", pokemonDAO.listAllPokemons());
        } catch (SQLException e) {
            model.addAttribute("allPokemons", new ArrayList<Pokemon>());
            logger.error("Error al obtener Pokémon: {}", e.getMessage());
        }
        return "route-form";
    }

    // Formulario para editar ruta existente
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la ruta con ID {}", id);
        try {
            Route route = routeDAO.getRouteWithPokemons(id); // Trae también los Pokémon asociados
            model.addAttribute("route", route);
            model.addAttribute("allPokemons", pokemonDAO.listAllPokemons());
        } catch (SQLException e) {
            model.addAttribute("route", new Route());
            model.addAttribute("allPokemons", new ArrayList<Pokemon>());
            model.addAttribute("errorMessage", "Error al obtener la ruta o Pokémon");
            logger.error("Error al obtener la ruta o Pokémon: {}", e.getMessage());
        }
        return "route-form";
    }

    // Insertar nueva ruta
    @PostMapping("/insert")
    public String insertRoute(@ModelAttribute("route") Route route, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva ruta...");
        try {
            routeDAO.insertRoute(route);
            logger.info("Ruta insertada con éxito.");
        } catch (SQLException e) {
            logger.error("Error al insertar la ruta: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al insertar la ruta.");
        }
        return "redirect:/routes";
    }

    // Actualizar ruta existente
    @PostMapping("/update")
    public String updateRoute(@ModelAttribute("route") Route route,
                              RedirectAttributes redirectAttributes) {
        logger.info("Actualizando ruta con ID {}", route.getId());
        try {
            routeDAO.updateRoute(route);
            logger.info("Ruta con ID {} actualizada con éxito.", route.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar la ruta con ID {}: {}", route.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la ruta.");
        }
        return "redirect:/routes";
    }

    // Eliminar ruta
    @PostMapping("/delete")
    public String deleteRoute(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando ruta con ID {}", id);
        try {
            routeDAO.deleteRoute(id);
            logger.info("Ruta con ID {} eliminada con éxito.", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar la ruta con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la ruta.");
        }
        return "redirect:/routes";
    }

    // Ver ruta con Pokémon asociados
    @GetMapping("/view")
    public String viewRoute(@RequestParam("id") Long id, Model model) {
        try {
            Route route = routeDAO.getRouteWithPokemons(id); // Trae los Pokémon de la ruta
            model.addAttribute("route", route);
        } catch (SQLException e) {
            model.addAttribute("errorMessage", "Error al obtener la ruta");
            logger.error("Error al obtener la ruta con ID {}: {}", id, e.getMessage());
        }
        return "route-view";
    }

}
