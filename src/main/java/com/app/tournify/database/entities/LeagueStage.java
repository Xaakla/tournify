package com.app.tournify.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class LeagueStage extends BaseTournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Round> rounds;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamTable> table;
}
