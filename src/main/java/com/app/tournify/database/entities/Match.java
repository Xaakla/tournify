package com.app.tournify.database.entities;

import com.app.tournify.enums.MatchStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Team home;

    @NotNull
    private int homeScore;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Team away;

    @NotNull
    private int awayScore;

    @NotNull
    private MatchStatus status;

    public Match() {
    }

    public Match(Team home, Team away) {
        this.home = home;
        this.homeScore = 0;
        this.away = away;
        this.awayScore = 0;
        this.status = MatchStatus.NOT_STARTED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getHome() {
        return home;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int home_score) {
        this.homeScore = home_score;
    }

    public Team getAway() {
        return away;
    }

    public void setAway(Team away) {
        this.away = away;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int away_score) {
        this.awayScore = away_score;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }
}
