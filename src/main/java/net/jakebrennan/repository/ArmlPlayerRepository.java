package net.jakebrennan.repository;

import java.util.List;
import java.util.Optional;
import net.jakebrennan.domain.ArmlPlayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArmlPlayer entity.
 *
 * When extending this class, extend ArmlPlayerRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ArmlPlayerRepository extends ArmlPlayerRepositoryWithBagRelationships, JpaRepository<ArmlPlayer, Long> {
    default Optional<ArmlPlayer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<ArmlPlayer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<ArmlPlayer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
