package com.app.tournify.services;

import com.app.tournify.database.entities.Team;
import com.app.tournify.database.repositories.TeamRepository;
import com.app.tournify.dtos.TeamDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    @Transactional
    public Team findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public List<Team> findAllTeamsByIds(List<Long> ids) {
        return teamRepository.findByIdIn(ids);
    }

    @Transactional
    public Team saveTeam(TeamDto team) {
        return teamRepository.save(new Team(team.getName(), team.getAcronym(), team.getImageId(), team.getColor()));
    }

}
