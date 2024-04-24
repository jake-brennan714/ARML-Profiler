package net.jakebrennan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArmlProfile.
 */
@Entity
@Table(name = "profiles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArmlProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    @Column(name = "win_rate")
    private Double winRate;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    @Column(name = "feed_rate")
    private Double feedRate;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    @Column(name = "call_rate")
    private Double callRate;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    @Column(name = "rii_rate")
    private Double riiRate;

    @Column(name = "feed_ev")
    private Long feedEV;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "armlGameScores", "games", "armlProfile" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ArmlPlayer playerID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArmlProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWinRate() {
        return this.winRate;
    }

    public ArmlProfile winRate(Double winRate) {
        this.setWinRate(winRate);
        return this;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Double getFeedRate() {
        return this.feedRate;
    }

    public ArmlProfile feedRate(Double feedRate) {
        this.setFeedRate(feedRate);
        return this;
    }

    public void setFeedRate(Double feedRate) {
        this.feedRate = feedRate;
    }

    public Double getCallRate() {
        return this.callRate;
    }

    public ArmlProfile callRate(Double callRate) {
        this.setCallRate(callRate);
        return this;
    }

    public void setCallRate(Double callRate) {
        this.callRate = callRate;
    }

    public Double getRiiRate() {
        return this.riiRate;
    }

    public ArmlProfile riiRate(Double riiRate) {
        this.setRiiRate(riiRate);
        return this;
    }

    public void setRiiRate(Double riiRate) {
        this.riiRate = riiRate;
    }

    public Long getFeedEV() {
        return this.feedEV;
    }

    public ArmlProfile feedEV(Long feedEV) {
        this.setFeedEV(feedEV);
        return this;
    }

    public void setFeedEV(Long feedEV) {
        this.feedEV = feedEV;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArmlProfile user(User user) {
        this.setUser(user);
        return this;
    }

    public ArmlPlayer getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(ArmlPlayer armlPlayer) {
        this.playerID = armlPlayer;
    }

    public ArmlProfile playerID(ArmlPlayer armlPlayer) {
        this.setPlayerID(armlPlayer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmlProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((ArmlProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmlProfile{" +
            "id=" + getId() +
            ", winRate=" + getWinRate() +
            ", feedRate=" + getFeedRate() +
            ", callRate=" + getCallRate() +
            ", riiRate=" + getRiiRate() +
            ", feedEV=" + getFeedEV() +
            "}";
    }
}
