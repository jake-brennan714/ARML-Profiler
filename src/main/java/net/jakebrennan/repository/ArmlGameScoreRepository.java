package net.jakebrennan.repository;

import net.jakebrennan.domain.ArmlGameScore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArmlGameScore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArmlGameScoreRepository extends JpaRepository<ArmlGameScore, Long> {}
