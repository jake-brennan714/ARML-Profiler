package net.jakebrennan.domain;

import static net.jakebrennan.domain.ArmlGameScoreTestSamples.*;
import static net.jakebrennan.domain.ArmlGameTestSamples.*;
import static net.jakebrennan.domain.ArmlPlayerTestSamples.*;
import static net.jakebrennan.domain.ArmlProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import net.jakebrennan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArmlPlayerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArmlPlayer.class);
        ArmlPlayer armlPlayer1 = getArmlPlayerSample1();
        ArmlPlayer armlPlayer2 = new ArmlPlayer();
        assertThat(armlPlayer1).isNotEqualTo(armlPlayer2);

        armlPlayer2.setId(armlPlayer1.getId());
        assertThat(armlPlayer1).isEqualTo(armlPlayer2);

        armlPlayer2 = getArmlPlayerSample2();
        assertThat(armlPlayer1).isNotEqualTo(armlPlayer2);
    }

    @Test
    void armlGameScoreTest() throws Exception {
        ArmlPlayer armlPlayer = getArmlPlayerRandomSampleGenerator();
        ArmlGameScore armlGameScoreBack = getArmlGameScoreRandomSampleGenerator();

        armlPlayer.addArmlGameScore(armlGameScoreBack);
        assertThat(armlPlayer.getArmlGameScores()).containsOnly(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlPlayer()).isEqualTo(armlPlayer);

        armlPlayer.removeArmlGameScore(armlGameScoreBack);
        assertThat(armlPlayer.getArmlGameScores()).doesNotContain(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlPlayer()).isNull();

        armlPlayer.armlGameScores(new HashSet<>(Set.of(armlGameScoreBack)));
        assertThat(armlPlayer.getArmlGameScores()).containsOnly(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlPlayer()).isEqualTo(armlPlayer);

        armlPlayer.setArmlGameScores(new HashSet<>());
        assertThat(armlPlayer.getArmlGameScores()).doesNotContain(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlPlayer()).isNull();
    }

    @Test
    void gamesTest() throws Exception {
        ArmlPlayer armlPlayer = getArmlPlayerRandomSampleGenerator();
        ArmlGame armlGameBack = getArmlGameRandomSampleGenerator();

        armlPlayer.addGames(armlGameBack);
        assertThat(armlPlayer.getGames()).containsOnly(armlGameBack);

        armlPlayer.removeGames(armlGameBack);
        assertThat(armlPlayer.getGames()).doesNotContain(armlGameBack);

        armlPlayer.games(new HashSet<>(Set.of(armlGameBack)));
        assertThat(armlPlayer.getGames()).containsOnly(armlGameBack);

        armlPlayer.setGames(new HashSet<>());
        assertThat(armlPlayer.getGames()).doesNotContain(armlGameBack);
    }

    @Test
    void armlProfileTest() throws Exception {
        ArmlPlayer armlPlayer = getArmlPlayerRandomSampleGenerator();
        ArmlProfile armlProfileBack = getArmlProfileRandomSampleGenerator();

        armlPlayer.setArmlProfile(armlProfileBack);
        assertThat(armlPlayer.getArmlProfile()).isEqualTo(armlProfileBack);
        assertThat(armlProfileBack.getPlayerID()).isEqualTo(armlPlayer);

        armlPlayer.armlProfile(null);
        assertThat(armlPlayer.getArmlProfile()).isNull();
        assertThat(armlProfileBack.getPlayerID()).isNull();
    }
}
