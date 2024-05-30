package com.app.tournify.services;

import com.app.tournify.database.entities.Team;
import com.app.tournify.database.repositories.TeamRepository;
import com.app.tournify.dtos.TeamDto;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UploadImageService uploadImageService;

    public TeamService(TeamRepository teamRepository, UploadImageService uploadImageService) {
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
        String imageUrl = uploadImageService.uploadImage(image);

        if (image.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        team.setImageId(imageUrl);
        teamRepository.save(team);

        return ResponseEntity.ok("Image uploaded successfully");

    }
}
