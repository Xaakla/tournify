package com.app.tournify.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class TeamTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private Team team;

    @NotNull
    private int points;

    @NotNull
    private int goalsScored;

    @NotNull
    private int goalsConceded;

    @NotNull
    private int goalDifference;

    @NotNull
    private int wins;

    @NotNull
    private int draws;

    @NotNull
    private int losses;

}
