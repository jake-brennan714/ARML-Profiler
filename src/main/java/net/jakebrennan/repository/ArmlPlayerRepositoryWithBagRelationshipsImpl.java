package net.jakebrennan.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import net.jakebrennan.domain.ArmlPlayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ArmlPlayerRepositoryWithBagRelationshipsImpl implements ArmlPlayerRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ARMLPLAYERS_PARAMETER = "armlPlayers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ArmlPlayer> fetchBagRelationships(Optional<ArmlPlayer> armlPlayer) {
        return armlPlayer.map(this::fetchGames);
    }

    @Override
    public Page<ArmlPlayer> fetchBagRelationships(Page<ArmlPlayer> armlPlayers) {
        return new PageImpl<>(fetchBagRelationships(armlPlayers.getContent()), armlPlayers.getPageable(), armlPlayers.getTotalElements());
    }

    @Override
    public List<ArmlPlayer> fetchBagRelationships(List<ArmlPlayer> armlPlayers) {
        return Optional.of(armlPlayers).map(this::fetchGames).orElse(Collections.emptyList());
    }

    ArmlPlayer fetchGames(ArmlPlayer result) {
        return entityManager
            .createQuery(
                "select armlPlayer from ArmlPlayer armlPlayer left join fetch armlPlayer.games where armlPlayer.id = :id",
                ArmlPlayer.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ArmlPlayer> fetchGames(List<ArmlPlayer> armlPlayers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, armlPlayers.size()).forEach(index -> order.put(armlPlayers.get(index).getId(), index));
        List<ArmlPlayer> result = entityManager
            .createQuery(
                "select armlPlayer from ArmlPlayer armlPlayer left join fetch armlPlayer.games where armlPlayer in :armlPlayers",
                ArmlPlayer.class
            )
            .setParameter(ARMLPLAYERS_PARAMETER, armlPlayers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
