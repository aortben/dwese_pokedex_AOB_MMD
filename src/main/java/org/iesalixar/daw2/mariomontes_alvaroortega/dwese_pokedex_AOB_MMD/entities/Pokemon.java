package org.iesalixar.daw2.mariomontes_alvaroortega.dwese_pokedex_AOB_MMD.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private int level;

    @ManyToMany(mappedBy = "pokemons")
    private List<Route> routes = new ArrayList<>();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }

    @ManyToMany
    @JoinTable(
            name = "pokemon_moves",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "move_id")
    )
    private List<Move> moves = new ArrayList<>();

    // Para el formulario (IDs)
    @Transient
    private List<Long> moveIds = new ArrayList<>();

    public List<Move> getMoves() { return moves; }
    public void setMoves(List<Move> moves) { this.moves = moves; }

    public List<Long> getMoveIds() { return moveIds; }
    public void setMoveIds(List<Long> moveIds) { this.moveIds = moveIds; }

    @Transient
    public String getMoveNames() {
        if (moves == null || moves.isEmpty()) return "";
        return moves.stream()
                .map(Move::getName)
                .collect(Collectors.joining(", "));
    }

}
