package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Region;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Repository
public class RegionDAOImpl implements RegionDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger =
            LoggerFactory.getLogger(RegionDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    // Inyección de JdbcTemplate
    public RegionDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todas las regiones de la base de datos.
     * @return Lista de regiones
     */
    @Override
    public List<Region> listAllRegions() {
        logger.info("Listing all regions from the database.");
        String sql = "SELECT * FROM region";
        List<Region> region = jdbcTemplate.query(sql, new
                BeanPropertyRowMapper<>(Region.class));
        logger.info("Retrieved {} regions from the database.", region.size());
        return region;
    }

    /**
     * Inserta una nueva región en la base de datos.
     * @param region Región a insertar

     */
    @Override
    public void insertRegion(Region region) {
        logger.info("Inserting region with name: {}",
                region.getName());
        String sql = "INSERT INTO region (name) VALUES (?)";
        int rowsAffected = jdbcTemplate.update(sql,
                region.getName());
        logger.info("Inserted region. Rows affected: {}", rowsAffected);
    }

    /**
     * Actualiza una región existente en la base de datos.
     * @param region Región a actualizar
     */
    @Override
    public void updateRegion(Region region) {
        logger.info("Updating region with id: {}", region.getId());
        String sql = "UPDATE region SET name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                region.getName(), region.getId());
        logger.info("Updated region. Rows affected: {}", rowsAffected);
    }

    /**
     * Elimina una región de la base de datos.
     * @param id ID de la región a eliminar
     */
    @Override
    public void deleteRegion(Long id) {
        logger.info("Deleting region with id: {}", id);
        String sqlDeleteRegion = "DELETE FROM region WHERE id = ?";
        int regionDeleted = jdbcTemplate.update(sqlDeleteRegion, id);
        logger.info("Deleted region. Rows affected: {}", regionDeleted);
    }

    /**
     * Recupera una región por su ID.
     * @param id ID de la región a recuperar
     * @return Región encontrada o null si no existe
     */
    @Override
    public Region getRegionById(Long id) {
        logger.info("Retrieving region by id: {}", id);
        String sql = "SELECT * FROM region WHERE id = ?";
            try {
            Region region = jdbcTemplate.queryForObject(sql, new
                    BeanPropertyRowMapper<>(Region.class), id);
            logger.info("Region retrieved: {}",
                    region.getName());
            return region;
        } catch (Exception e) {
            logger.warn("No region found with id: {}", id);
            return null;
        }
    }

}