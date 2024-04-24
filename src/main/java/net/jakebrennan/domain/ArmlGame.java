package net.jakebrennan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArmlGame.
 */
@Entity
@Table(name = "games")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArmlGame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "game_id", nullable = false, unique = true)
    private Long gameID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "armlGame")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "armlGame", "armlPlayer" }, allowSetters = true)
    private Set<ArmlGameScore> armlGameScores = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "games")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "armlGameScores", "games", "armlProfile" }, allowSetters = true)
    private Set<ArmlPlayer> players = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArmlGame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameID() {
        return this.gameID;
    }

    public ArmlGame gameID(Long gameID) {
        this.setGameID(gameID);
        return this;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    public Set<ArmlGameScore> getArmlGameScores() {
        return this.armlGameScores;
    }

    public void setArmlGameScores(Set<ArmlGameScore> armlGameScores) {
        if (this.armlGameScores != null) {
            this.armlGameScores.forEach(i -> i.setArmlGame(null));
        }
        if (armlGameScores != null) {
            armlGameScores.forEach(i -> i.setArmlGame(this));
        }
        this.armlGameScores = armlGameScores;
    }

    public ArmlGame armlGameScores(Set<ArmlGameScore> armlGameScores) {
        this.setArmlGameScores(armlGameScores);
        return this;
    }

    public ArmlGame addArmlGameScore(ArmlGameScore armlGameScore) {
        this.armlGameScores.add(armlGameScore);
        armlGameScore.setArmlGame(this);
        return this;
    }

    public ArmlGame removeArmlGameScore(ArmlGameScore armlGameScore) {
        this.armlGameScores.remove(armlGameScore);
        armlGameScore.setArmlGame(null);
        return this;
    }

    public Set<ArmlPlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<ArmlPlayer> armlPlayers) {
        if (this.players != null) {
            this.players.forEach(i -> i.removeGames(this));
        }
        if (armlPlayers != null) {
            armlPlayers.forEach(i -> i.addGames(this));
        }
        this.players = armlPlayers;
    }

    public ArmlGame players(Set<ArmlPlayer> armlPlayers) {
        this.setPlayers(armlPlayers);
        return this;
    }

    public ArmlGame addPlayers(ArmlPlayer armlPlayer) {
        this.players.add(armlPlayer);
        armlPlayer.getGames().add(this);
        return this;
    }

    public ArmlGame removePlayers(ArmlPlayer armlPlayer) {
        this.players.remove(armlPlayer);
        armlPlayer.getGames().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmlGame)) {
            return false;
        }
        return getId() != null && getId().equals(((ArmlGame) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmlGame{" +
            "id=" + getId() +
            ", gameID=" + getGameID() +
            "}";
    }
}
