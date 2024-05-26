package com.app.tournify.database.repositories;

import com.app.tournify.database.entities.LeagueStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueStageRepository extends JpaRepository<LeagueStage, Long> {
}
