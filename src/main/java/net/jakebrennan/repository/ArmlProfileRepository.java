package net.jakebrennan.repository;

import net.jakebrennan.domain.ArmlProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArmlProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArmlProfileRepository extends JpaRepository<ArmlProfile, Long> {}
