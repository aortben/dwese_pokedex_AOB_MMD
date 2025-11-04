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
    public String insertRoute(@ModelAttribute Route route, RedirectAttributes flash) {

        List<Pokemon> pokemons = new ArrayList<>();
        if (route.getPokemonsIds() != null) {
            for (Long id : route.getPokemonsIds()) {
                Pokemon p = pokemonDAO.getPokemonById(id);
                if (p != null) pokemons.add(p);
            }
        }
        route.setPokemons(pokemons);
        routeDAO.insertRoute(route);

        flash.addFlashAttribute("success", "Ruta creada correctamente.");
        return "redirect:/routes";
    }

    // Actualizar ruta existente
    @PostMapping("/update")
    public String updateRoute(@ModelAttribute Route route, RedirectAttributes flash) {

        List<Pokemon> pokemons = new ArrayList<>();
        if (route.getPokemonsIds() != null) {
            for (Long id : route.getPokemonsIds()) {
                Pokemon p = pokemonDAO.getPokemonById(id);
                if (p != null) pokemons.add(p);
            }
        }
        route.setPokemons(pokemons);

        routeDAO.updateRoute(route);

        flash.addFlashAttribute("success", "Ruta actualizada correctamente.");
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
