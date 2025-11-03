package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.controllers;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao.RegionDAO;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Region;
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
@RequestMapping("/regions")
public class RegionController {

    private static final Logger logger =
            LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionDAO regionDAO;

    @GetMapping
    public String listRegions(Model model) {
        logger.info("Solicitando la lista de todas las regiones...");
        List<Region> listRegions = null;
        try {
            listRegions = regionDAO.listAllRegions();
            logger.info("Se han cargado {} regiones.", listRegions.size());
        } catch (SQLException e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las regiones.");
        }
        model.addAttribute("regionList", listRegions);
        return "region";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva región.");

        model.addAttribute("region", new Region());
        return "region-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la región con ID {}", id);
        Region region = null;
        try {
            region = regionDAO.getRegionById((long) id);
            if (region == null) {
                logger.warn("No se encontró la región con ID {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener la región con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la región.");
        }
        model.addAttribute("region", region);

        return "region-form";
    }

    @PostMapping("/insert")
    public String insertRegion(@ModelAttribute("region") Region region, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva región...");
        try {
            // Aquí, si existiera el método existsRegionByCode, eliminarlo o ajustarlo
            // Si no es necesario, simplemente insertamos la región
            regionDAO.insertRegion(region);
            logger.info("Región insertada con éxito.");
        } catch (SQLException e) {
            logger.error("Error al insertar la región: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al insertar la región.");
        }
        return "redirect:/regions";
    }

    @PostMapping("/update")
    public String updateRegion(@ModelAttribute("region") Region region,
                               RedirectAttributes redirectAttributes) {
        logger.info("Actualizando región con ID {}", region.getId());
        try {
            // Ajuste si no existe validación por code
            regionDAO.updateRegion(region);
            logger.info("Región con ID {} actualizada con éxito.", region.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar la región con ID {}: {}", region.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la región.");
        }
        return "redirect:/regions";
    }

    @PostMapping("/delete")
    public String deleteRegion(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando región con ID {}", id);
        try {
            regionDAO.deleteRegion(id);
            logger.info("Región con ID {} eliminada con éxito.", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la región.");
        }
        return "redirect:/regions";
    }
}