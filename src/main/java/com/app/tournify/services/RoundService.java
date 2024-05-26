package com.app.tournify.services;

import com.app.tournify.database.entities.Round;
import com.app.tournify.database.repositories.RoundRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @Transactional
    public Round save(Round round) {
        return roundRepository.save(round);
    }
}
