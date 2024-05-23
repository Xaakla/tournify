package com.app.tournify.database.entities;

import com.app.tournify.enums.TournamentType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@MappedSuperclass
public class BaseTournament {
    @NotBlank
    private String name;

    @NotNull
    private TournamentType type;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams;
}
