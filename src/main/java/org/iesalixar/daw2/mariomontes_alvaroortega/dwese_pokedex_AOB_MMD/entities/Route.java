package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}