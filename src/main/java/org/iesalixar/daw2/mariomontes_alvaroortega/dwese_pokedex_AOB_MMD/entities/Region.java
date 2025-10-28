package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Region {
    private Long id;
    private String name;

    public Region(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}