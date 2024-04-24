package net.jakebrennan.domain;

import static net.jakebrennan.domain.ArmlGameScoreTestSamples.*;
import static net.jakebrennan.domain.ArmlGameTestSamples.*;
import static net.jakebrennan.domain.ArmlPlayerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import net.jakebrennan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArmlGameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArmlGame.class);
        ArmlGame armlGame1 = getArmlGameSample1();
        ArmlGame armlGame2 = new ArmlGame();
        assertThat(armlGame1).isNotEqualTo(armlGame2);

        armlGame2.setId(armlGame1.getId());
        assertThat(armlGame1).isEqualTo(armlGame2);

        armlGame2 = getArmlGameSample2();
        assertThat(armlGame1).isNotEqualTo(armlGame2);
    }

    @Test
    void armlGameScoreTest() throws Exception {
        ArmlGame armlGame = getArmlGameRandomSampleGenerator();
        ArmlGameScore armlGameScoreBack = getArmlGameScoreRandomSampleGenerator();

        armlGame.addArmlGameScore(armlGameScoreBack);
        assertThat(armlGame.getArmlGameScores()).containsOnly(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlGame()).isEqualTo(armlGame);

        armlGame.removeArmlGameScore(armlGameScoreBack);
        assertThat(armlGame.getArmlGameScores()).doesNotContain(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlGame()).isNull();

        armlGame.armlGameScores(new HashSet<>(Set.of(armlGameScoreBack)));
        assertThat(armlGame.getArmlGameScores()).containsOnly(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlGame()).isEqualTo(armlGame);

        armlGame.setArmlGameScores(new HashSet<>());
        assertThat(armlGame.getArmlGameScores()).doesNotContain(armlGameScoreBack);
        assertThat(armlGameScoreBack.getArmlGame()).isNull();
    }

    @Test
    void playersTest() throws Exception {
        ArmlGame armlGame = getArmlGameRandomSampleGenerator();
        ArmlPlayer armlPlayerBack = getArmlPlayerRandomSampleGenerator();

        armlGame.addPlayers(armlPlayerBack);
        assertThat(armlGame.getPlayers()).containsOnly(armlPlayerBack);
        assertThat(armlPlayerBack.getGames()).containsOnly(armlGame);

        armlGame.removePlayers(armlPlayerBack);
        assertThat(armlGame.getPlayers()).doesNotContain(armlPlayerBack);
        assertThat(armlPlayerBack.getGames()).doesNotContain(armlGame);

        armlGame.players(new HashSet<>(Set.of(armlPlayerBack)));
        assertThat(armlGame.getPlayers()).containsOnly(armlPlayerBack);
        assertThat(armlPlayerBack.getGames()).containsOnly(armlGame);

        armlGame.setPlayers(new HashSet<>());
        assertThat(armlGame.getPlayers()).doesNotContain(armlPlayerBack);
        assertThat(armlPlayerBack.getGames()).doesNotContain(armlGame);
    }
}
