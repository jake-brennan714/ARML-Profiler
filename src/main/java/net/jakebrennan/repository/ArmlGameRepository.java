package net.jakebrennan.repository;

import net.jakebrennan.domain.ArmlGame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArmlGame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArmlGameRepository extends JpaRepository<ArmlGame, Long> {}
