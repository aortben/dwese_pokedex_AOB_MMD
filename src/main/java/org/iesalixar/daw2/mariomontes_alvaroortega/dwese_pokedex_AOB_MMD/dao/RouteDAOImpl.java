package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Pokemon;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Route;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
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
        String sql = "INSERT INTO route (name) VALUES (?)";
        jdbcTemplate.update(sql, route.getName());

        Long routeId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        route.setId(routeId);

        insertRoutePokemons(route);
    }

    /**
     * Actualiza una ruta existente en la base de datos.
     * @param route Rute a actualizar
     */
    @Override
    public void updateRoute(Route route) {
        String sql = "UPDATE route SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, route.getName(), route.getId());

        // borrar relaciones anteriores
        jdbcTemplate.update("DELETE FROM route_pokemon WHERE route_id = ?", route.getId());

        // insertar las nuevas
        insertRoutePokemons(route);
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

    private void insertRoutePokemons(Route route) {
        if (route.getPokemons() == null) return;

        for (Pokemon p : route.getPokemons()) {
            jdbcTemplate.update(
                    "INSERT INTO route_pokemon (route_id, pokemon_id) VALUES (?, ?)",
                    route.getId(), p.getId()
            );
        }
    }

    public Route getRouteWithPokemons(Long id) {
        Route route = getRouteById(id);
        if (route == null) return null;

        String sql = """
        SELECT p.id, p.name
        FROM pokemon p
        JOIN route_pokemon rp ON p.id = rp.pokemon_id
        WHERE rp.route_id = ?
    """;

        List<Pokemon> pokemons = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Pokemon.class), id);
        route.setPokemons(pokemons);
        route.setPokemonsIds(pokemons.stream().map(Pokemon::getId).toList());

        return route;
    }

    @Override
    public void insertRoute(String name, Long regionId) {
        String sql = "INSERT INTO route (name, region_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, name, regionId);
    }

}