package net.jakebrennan.domain;

import static net.jakebrennan.domain.ArmlGameScoreTestSamples.*;
import static net.jakebrennan.domain.ArmlGameTestSamples.*;
import static net.jakebrennan.domain.ArmlPlayerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.jakebrennan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArmlGameScoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArmlGameScore.class);
        ArmlGameScore armlGameScore1 = getArmlGameScoreSample1();
        ArmlGameScore armlGameScore2 = new ArmlGameScore();
        assertThat(armlGameScore1).isNotEqualTo(armlGameScore2);

        armlGameScore2.setId(armlGameScore1.getId());
        assertThat(armlGameScore1).isEqualTo(armlGameScore2);

        armlGameScore2 = getArmlGameScoreSample2();
        assertThat(armlGameScore1).isNotEqualTo(armlGameScore2);
    }

    @Test
    void armlGameTest() throws Exception {
        ArmlGameScore armlGameScore = getArmlGameScoreRandomSampleGenerator();
        ArmlGame armlGameBack = getArmlGameRandomSampleGenerator();

        armlGameScore.setArmlGame(armlGameBack);
        assertThat(armlGameScore.getArmlGame()).isEqualTo(armlGameBack);

        armlGameScore.armlGame(null);
        assertThat(armlGameScore.getArmlGame()).isNull();
    }

    @Test
    void armlPlayerTest() throws Exception {
        ArmlGameScore armlGameScore = getArmlGameScoreRandomSampleGenerator();
        ArmlPlayer armlPlayerBack = getArmlPlayerRandomSampleGenerator();

        armlGameScore.setArmlPlayer(armlPlayerBack);
        assertThat(armlGameScore.getArmlPlayer()).isEqualTo(armlPlayerBack);

        armlGameScore.armlPlayer(null);
        assertThat(armlGameScore.getArmlPlayer()).isNull();
    }
}
