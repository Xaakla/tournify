package com.app.tournify.controllers;

import com.app.tournify.database.entities.LeagueStage;
import com.app.tournify.dtos.NewEditLeagueStageDto;
import com.app.tournify.services.LeagueStageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/league-stage")
public class LeagueStageController {

    private final LeagueStageService leagueStageService;

    public LeagueStageController(LeagueStageService leagueStageService) {
        this.leagueStageService = leagueStageService;
    }

    @GetMapping()
    public ResponseEntity<List<LeagueStage>> findAllLeagueStages() {
        return ResponseEntity.ok(leagueStageService.findAllLeagueStages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueStage> findLeagueStageById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leagueStageService.findLeagueStageById(id));
    }

    @PostMapping()
    public ResponseEntity<LeagueStage> createLeagueStage(@RequestBody NewEditLeagueStageDto leagueStage) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueStageService.saveLeagueStage(leagueStage));
    }
}
