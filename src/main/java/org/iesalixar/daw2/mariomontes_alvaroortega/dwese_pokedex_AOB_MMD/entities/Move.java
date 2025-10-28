package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.enums.MoveCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Enumerated(EnumType.STRING)
    private MoveCategory category;

    private int power;
    private int accuracy;
    private int pp;
}
