package net.jakebrennan.repository;

import java.util.List;
import java.util.Optional;
import net.jakebrennan.domain.ArmlPlayer;
import org.springframework.data.domain.Page;

public interface ArmlPlayerRepositoryWithBagRelationships {
    Optional<ArmlPlayer> fetchBagRelationships(Optional<ArmlPlayer> armlPlayer);

    List<ArmlPlayer> fetchBagRelationships(List<ArmlPlayer> armlPlayers);

    Page<ArmlPlayer> fetchBagRelationships(Page<ArmlPlayer> armlPlayers);
}
