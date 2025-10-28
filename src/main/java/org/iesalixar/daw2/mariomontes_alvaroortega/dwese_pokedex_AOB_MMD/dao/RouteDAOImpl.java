package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Route;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Repository
public class RouteDAOImpl implements RouteDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger =
            LoggerFactory.getLogger(RouteDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    // Inyección de JdbcTemplate
    public RouteDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todas las rutas de la base de datos.
     * @return Lista de rutas
     */
    @Override
    public List<Route> listAllRoutes() {
        logger.info("Listing all routes from the database.");
        String sql = "SELECT * FROM route";
        List<Route> route = jdbcTemplate.query(sql, new
                BeanPropertyRowMapper<>(Route.class));
        logger.info("Retrieved {} routes from the database.", route.size());
        return route;
    }

    /**
     * Inserta una nueva ruta en la base de datos.
     * @param route Ruta a insertar

     */
    @Override
    public void insertRoute(Route route) {
        logger.info("Inserting route with name: {}",
                route.getName());
        String sql = "INSERT INTO route (name) VALUES (?)";
        int rowsAffected = jdbcTemplate.update(sql,
                route.getName());
        logger.info("Inserted route. Rows affected: {}", rowsAffected);
    }

    /**
     * Actualiza una ruta existente en la base de datos.
     * @param route Rute a actualizar
     */
    @Override
    public void updateRoute(Route route) {
        logger.info("Updating region with id: {}", route.getId());
        String sql = "UPDATE route SET name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                route.getName(), route.getId());
        logger.info("Updated region. Rows affected: {}", rowsAffected);
    }

    /**
     * Elimina una ruta de la base de datos.
     * @param id ID de la ruta a eliminar
     */
    @Override

    public void deleteRoute(Long id) {
        logger.info("Deleting route with id: {}", id);
        String sql = "DELETE FROM route WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted route. Rows affected: {}", rowsAffected);
    }

    /**
     * Recupera una región por su ID.
     * @param id ID de la región a recuperar
     * @return Región encontrada o null si no existe
     */
    @Override
    public Route getRouteById(Long id) {
        logger.info("Retrieving route by id: {}", id);
        String sql = "SELECT * FROM route WHERE id = ?";
        try {
            Route route = jdbcTemplate.queryForObject(sql, new
                    BeanPropertyRowMapper<>(Route.class), id);
            logger.info("Region retrieved: {}",
                    route.getName());
            return route;
        } catch (Exception e) {
            logger.warn("No region found with id: {}", id);
            return null;
        }
    }

}