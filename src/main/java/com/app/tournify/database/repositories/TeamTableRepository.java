package com.app.tournify.database.repositories;

import com.app.tournify.database.entities.TeamTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamTableRepository extends JpaRepository<TeamTable, Long> {
}
