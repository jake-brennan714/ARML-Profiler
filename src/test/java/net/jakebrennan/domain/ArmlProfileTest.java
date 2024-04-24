package net.jakebrennan.domain;

import static net.jakebrennan.domain.ArmlPlayerTestSamples.*;
import static net.jakebrennan.domain.ArmlProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.jakebrennan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArmlProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArmlProfile.class);
        ArmlProfile armlProfile1 = getArmlProfileSample1();
        ArmlProfile armlProfile2 = new ArmlProfile();
        assertThat(armlProfile1).isNotEqualTo(armlProfile2);

        armlProfile2.setId(armlProfile1.getId());
        assertThat(armlProfile1).isEqualTo(armlProfile2);

        armlProfile2 = getArmlProfileSample2();
        assertThat(armlProfile1).isNotEqualTo(armlProfile2);
    }

    @Test
    void playerIDTest() throws Exception {
        ArmlProfile armlProfile = getArmlProfileRandomSampleGenerator();
        ArmlPlayer armlPlayerBack = getArmlPlayerRandomSampleGenerator();

        armlProfile.setPlayerID(armlPlayerBack);
        assertThat(armlProfile.getPlayerID()).isEqualTo(armlPlayerBack);

        armlProfile.playerID(null);
        assertThat(armlProfile.getPlayerID()).isNull();
    }
}
