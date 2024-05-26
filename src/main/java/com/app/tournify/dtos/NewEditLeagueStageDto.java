package com.app.tournify.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class NewEditLeagueStageDto extends BaseTournamentDto {
    @NotNull
    private List<Long> teamIds;

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }
}
