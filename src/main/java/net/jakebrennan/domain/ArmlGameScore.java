package net.jakebrennan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArmlGameScore.
 */
@Entity
@Table(name = "scores")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArmlGameScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "score", nullable = false)
    private Long score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "armlGameScores", "players" }, allowSetters = true)
    private ArmlGame armlGame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "armlGameScores", "games", "armlProfile" }, allowSetters = true)
    private ArmlPlayer armlPlayer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArmlGameScore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScore() {
        return this.score;
    }

    public ArmlGameScore score(Long score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public ArmlGame getArmlGame() {
        return this.armlGame;
    }

    public void setArmlGame(ArmlGame armlGame) {
        this.armlGame = armlGame;
    }

    public ArmlGameScore armlGame(ArmlGame armlGame) {
        this.setArmlGame(armlGame);
        return this;
    }

    public ArmlPlayer getArmlPlayer() {
        return this.armlPlayer;
    }

    public void setArmlPlayer(ArmlPlayer armlPlayer) {
        this.armlPlayer = armlPlayer;
    }

    public ArmlGameScore armlPlayer(ArmlPlayer armlPlayer) {
        this.setArmlPlayer(armlPlayer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmlGameScore)) {
            return false;
        }
        return getId() != null && getId().equals(((ArmlGameScore) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmlGameScore{" +
            "id=" + getId() +
            ", score=" + getScore() +
            "}";
    }
}
