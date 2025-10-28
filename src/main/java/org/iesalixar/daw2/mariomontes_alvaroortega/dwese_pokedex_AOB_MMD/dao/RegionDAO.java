package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Region;
import java.sql.SQLException;
import java.util.List;
public interface RegionDAO {
    List<Region> listAllRegions() throws SQLException;

    void insertRegion(Region region) throws SQLException;

    void updateRegion(Region region) throws SQLException;

    void deleteRegion(Long id) throws SQLException;

    Region getRegionById(Long id) throws SQLException;

}