package com.app.tournify.controllers;

import com.app.tournify.database.entities.Team;
import com.app.tournify.dtos.TeamDto;
import com.app.tournify.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping()
    public ResponseEntity<List<Team>> findAllTeams() {
        return ResponseEntity.ok(teamService.findAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> findTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.findTeamById(id));
    }

    @PostMapping()
    public ResponseEntity<Team> createTeam(@RequestBody TeamDto team) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.teamService.saveTeam(team));
    }
}