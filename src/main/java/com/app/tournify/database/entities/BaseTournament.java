package com.app.tournify.database.entities;

import com.app.tournify.enums.LegType;
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
    private LegType legType;

    @NotNull
    private int winPoints;

    @NotNull
    private int drawPoints;

    @NotNull
    private int lossPoints;

    public BaseTournament(String name, TournamentType type, LegType legType, int winPoints, int drawPoints, int lossPoints) {
        this.name = name;
        this.type = type;
        this.legType = legType;
        this.winPoints = winPoints;
        this.drawPoints = drawPoints;
        this.lossPoints = lossPoints;
    }

    public BaseTournament() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TournamentType getType() {
        return type;
    }

    public void setType(TournamentType type) {
        this.type = type;
    }

    public LegType getLegType() {
        return legType;
    }

    public void setLegType(LegType legType) {
        this.legType = legType;
    }

    public int getWinPoints() {
        return winPoints;
    }

    public void setWinPoints(int winPoints) {
        this.winPoints = winPoints;
    }

    public int getDrawPoints() {
        return drawPoints;
    }

    public void setDrawPoints(int drawPoints) {
        this.drawPoints = drawPoints;
    }

    public int getLossPoints() {
        return lossPoints;
    }

    public void setLossPoints(int lossPoints) {
        this.lossPoints = lossPoints;
    }
}
