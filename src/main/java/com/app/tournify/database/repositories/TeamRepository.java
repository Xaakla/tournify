package com.app.tournify.database.repositories;

import com.app.tournify.database.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByIdIn(List<Long> ids);
}
