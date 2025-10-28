package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.dao;

import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities.Route;
import java.sql.SQLException;
import java.util.List;
public interface RouteDAO {
    List<Route> listAllRoutes() throws SQLException;

    void insertRoute(Route route) throws SQLException;

    void updateRoute(Route route) throws SQLException;

    void deleteRoute(Long id) throws SQLException;

    Route getRouteById(Long id) throws SQLException;

}