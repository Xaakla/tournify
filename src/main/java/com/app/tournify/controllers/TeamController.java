package com.app.tournify.controllers;

import com.app.tournify.database.entities.Team;
import com.app.tournify.dtos.TeamDto;
import com.app.tournify.services.ImageService;
import com.app.tournify.services.TeamService;
import com.app.tournify.utils.Utils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final ImageService imageService;

    public TeamController(TeamService teamService, ImageService imageService) {
        this.teamService = teamService;
        this.imageService = imageService;
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

    @GetMapping("/{id}/image")
    public ResponseEntity<ByteArrayResource> downloadTeamImage(@PathVariable ("id") Long id) throws IOException {
        var team = teamService.findTeamById(id);

        var filenameSplit = team.getImageId().split(Pattern.quote("\\"));
        var filename = filenameSplit[filenameSplit.length - 1];
        var mediaType = "image/" + Utils.getExtension(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", filename))
                .body(new ByteArrayResource(Files.readAllBytes(Paths.get(team.getImageId()))));

    }
}
