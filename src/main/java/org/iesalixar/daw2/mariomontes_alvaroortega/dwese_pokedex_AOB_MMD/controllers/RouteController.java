package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.controllers;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao.RouteDAO;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private static final Logger logger =
            LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private RouteDAO routeDAO;

    @GetMapping
    public String listRoutes(Model model) {
        logger.info("Solicitando la lista de todas las rutas...");
        List<Route> listRoutes = null;
        try {
            listRoutes = routeDAO.listAllRoutes();
            logger.info("Se han cargado {} rutas.", listRoutes.size());
        } catch (SQLException e) {
            logger.error("Error al listar las rutas: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las rutas.");
        }
        model.addAttribute("listRoutes", listRoutes);
        return "route";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva ruta.");

        model.addAttribute("route", new Route());
        return "route-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la ruta con ID {}", id);
        Route route = null;
        try {
            route = routeDAO.getRouteById((long) id);
            if (route == null) {
                logger.warn("No se encontró la ruta con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la ruta con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la ruta.");
        }
        model.addAttribute("route", route);

        return "route-form";
    }

    @PostMapping("/insert")
    public String insertRoute(@ModelAttribute("route") Route route, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva ruta...");
        try {
            // Aquí, si existiera el método existsRouteByCode, eliminarlo o ajustarlo
            // Si no es necesario, simplemente insertamos la ruta
            routeDAO.insertRoute(route);
            logger.info("Ruta insertada con éxito.");
        } catch (SQLException e) {
            logger.error("Error al insertar la ruta: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al insertar la ruta.");
        }
        return "redirect:/routes";
    }

    @PostMapping("/update")
    public String updateRoute(@ModelAttribute("route") Route route,
                              RedirectAttributes redirectAttributes) {
        logger.info("Actualizando ruta con ID {}", route.getId());
        try {
            // Ajuste si no existe validación por code
            routeDAO.updateRoute(route);
            logger.info("Ruta con ID {} actualizada con éxito.", route.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar la ruta con ID {}: {}", route.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la ruta.");
        }
        return "redirect:/routes";
    }

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
}