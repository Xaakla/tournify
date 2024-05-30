package com.app.tournify.controllers;

import com.app.tournify.database.entities.Team;
import com.app.tournify.dtos.TeamDto;
import com.app.tournify.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.saveTeam(team));
    }

    @PostMapping("/all")
    public ResponseEntity<List<Team>> createTeams(@RequestBody List<TeamDto> teams) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.saveAllTeams(teams));
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage (@PathVariable ("id") Long id, @RequestParam("image") MultipartFile image) {
        return teamService.setTeamImage(id, image);
    }
}
