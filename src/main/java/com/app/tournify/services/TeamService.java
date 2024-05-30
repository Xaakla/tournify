package com.app.tournify.services;

import com.app.tournify.database.entities.Team;
import com.app.tournify.database.repositories.TeamRepository;
import com.app.tournify.dtos.TeamDto;
import com.app.tournify.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final ImageService uploadImageService;

    public TeamService(TeamRepository teamRepository, ImageService uploadImageService) {
        this.teamRepository = teamRepository;
        this.uploadImageService = uploadImageService;
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

    @Transactional
    public List<Team> saveAllTeams(List<TeamDto> teams) {
        var teamsToSave = teams.stream().map(team -> new Team(team.getName(), team.getAcronym(), team.getImageId(), team.getColor())).toList();
        return teamRepository.saveAll(teamsToSave);
    }

    public ResponseEntity<String> setTeamImage(Long id, MultipartFile image) {
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new RuntimeException("Invalid image type");
        }

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        new File(team.getImageId()).delete();

        String imageUrl = uploadImageService.uploadImage(image, String.format("team-%s.%s", id, Utils.getExtension(Objects.requireNonNull(image.getOriginalFilename()))));

        if (image.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        team.setImageId(imageUrl);
        teamRepository.save(team);

        return ResponseEntity.ok("Image uploaded successfully");

    }
}
