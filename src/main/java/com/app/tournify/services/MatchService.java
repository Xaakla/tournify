package com.app.tournify.services;

import com.app.tournify.database.entities.Match;
import com.app.tournify.database.repositories.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional
    public Match save(Match match) {
        return matchRepository.save(match);
    }
}
