package net.jakebrennan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import net.jakebrennan.domain.enumeration.ArmlLeague;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArmlPlayer.
 */
@Entity
@Table(name = "players")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArmlPlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "player_id", nullable = false, unique = true)
    private Long playerID;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 8)
    @Pattern(regexp = "\\S+")
    @Column(name = "tenhou_name", length = 8)
    private String tenhouName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "league", nullable = false)
    private ArmlLeague league;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "armlPlayer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "armlGame", "armlPlayer" }, allowSetters = true)
    private Set<ArmlGameScore> armlGameScores = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_players__games",
        joinColumns = @JoinColumn(name = "players_id"),
        inverseJoinColumns = @JoinColumn(name = "games_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "armlGameScores", "players" }, allowSetters = true)
    private Set<ArmlGame> games = new HashSet<>();

    @JsonIgnoreProperties(value = { "user", "playerID" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "playerID")
    private ArmlProfile armlProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArmlPlayer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerID() {
        return this.playerID;
    }

    public ArmlPlayer playerID(Long playerID) {
        this.setPlayerID(playerID);
        return this;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public ArmlPlayer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public ArmlPlayer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTenhouName() {
        return this.tenhouName;
    }

    public ArmlPlayer tenhouName(String tenhouName) {
        this.setTenhouName(tenhouName);
        return this;
    }

    public void setTenhouName(String tenhouName) {
        this.tenhouName = tenhouName;
    }

    public ArmlLeague getLeague() {
        return this.league;
    }

    public ArmlPlayer league(ArmlLeague league) {
        this.setLeague(league);
        return this;
    }

    public void setLeague(ArmlLeague league) {
        this.league = league;
    }

    public Set<ArmlGameScore> getArmlGameScores() {
        return this.armlGameScores;
    }

    public void setArmlGameScores(Set<ArmlGameScore> armlGameScores) {
        if (this.armlGameScores != null) {
            this.armlGameScores.forEach(i -> i.setArmlPlayer(null));
        }
        if (armlGameScores != null) {
            armlGameScores.forEach(i -> i.setArmlPlayer(this));
        }
        this.armlGameScores = armlGameScores;
    }

    public ArmlPlayer armlGameScores(Set<ArmlGameScore> armlGameScores) {
        this.setArmlGameScores(armlGameScores);
        return this;
    }

    public ArmlPlayer addArmlGameScore(ArmlGameScore armlGameScore) {
        this.armlGameScores.add(armlGameScore);
        armlGameScore.setArmlPlayer(this);
        return this;
    }

    public ArmlPlayer removeArmlGameScore(ArmlGameScore armlGameScore) {
        this.armlGameScores.remove(armlGameScore);
        armlGameScore.setArmlPlayer(null);
        return this;
    }

    public Set<ArmlGame> getGames() {
        return this.games;
    }

    public void setGames(Set<ArmlGame> armlGames) {
        this.games = armlGames;
    }

    public ArmlPlayer games(Set<ArmlGame> armlGames) {
        this.setGames(armlGames);
        return this;
    }

    public ArmlPlayer addGames(ArmlGame armlGame) {
        this.games.add(armlGame);
        return this;
    }

    public ArmlPlayer removeGames(ArmlGame armlGame) {
        this.games.remove(armlGame);
        return this;
    }

    public ArmlProfile getArmlProfile() {
        return this.armlProfile;
    }

    public void setArmlProfile(ArmlProfile armlProfile) {
        if (this.armlProfile != null) {
            this.armlProfile.setPlayerID(null);
        }
        if (armlProfile != null) {
            armlProfile.setPlayerID(this);
        }
        this.armlProfile = armlProfile;
    }

    public ArmlPlayer armlProfile(ArmlProfile armlProfile) {
        this.setArmlProfile(armlProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmlPlayer)) {
            return false;
        }
        return getId() != null && getId().equals(((ArmlPlayer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmlPlayer{" +
            "id=" + getId() +
            ", playerID=" + getPlayerID() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", tenhouName='" + getTenhouName() + "'" +
            ", league='" + getLeague() + "'" +
            "}";
    }
}
