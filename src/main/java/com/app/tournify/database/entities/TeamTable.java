package com.app.tournify.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class TeamTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
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

    @NotNull
    private int matchesPlayed;

    public TeamTable() {
    }

    public TeamTable(Team team) {
        this.team = team;
        this.points = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
        this.goalDifference = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.matchesPlayed = 0;
    }

    public TeamTable(String bye) {
        this.team = new Team(bye);
        this.points = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
        this.goalDifference = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.matchesPlayed = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }
}
