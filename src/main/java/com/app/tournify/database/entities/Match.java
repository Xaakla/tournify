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
    @OneToOne(fetch = FetchType.EAGER)
    private Team home;

    @NotNull
    private int home_score;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private Team away;

    @NotNull
    private int away_score;

    private MatchStatus status;
}
