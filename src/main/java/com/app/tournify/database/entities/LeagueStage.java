package com.app.tournify.database.entities;

import com.app.tournify.enums.LegType;
import com.app.tournify.enums.TournamentType;
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

    public LeagueStage(String name, TournamentType type, LegType legType, int winPoints, int drawPoints, int lossPoints) {
        super(name, type, legType, winPoints, drawPoints, lossPoints);
    }

    public LeagueStage() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<TeamTable> getTable() {
        return table;
    }

    public void setTable(List<TeamTable> table) {
        this.table = table;
    }
}
