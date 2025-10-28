package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Route {
    private Long id;
    private String name;

    public Route(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}