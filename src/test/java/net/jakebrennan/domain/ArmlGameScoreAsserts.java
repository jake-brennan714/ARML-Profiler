package net.jakebrennan.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ArmlGameScoreAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArmlGameScoreAllPropertiesEquals(ArmlGameScore expected, ArmlGameScore actual) {
        assertArmlGameScoreAutoGeneratedPropertiesEquals(expected, actual);
        assertArmlGameScoreAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArmlGameScoreAllUpdatablePropertiesEquals(ArmlGameScore expected, ArmlGameScore actual) {
        assertArmlGameScoreUpdatableFieldsEquals(expected, actual);
        assertArmlGameScoreUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArmlGameScoreAutoGeneratedPropertiesEquals(ArmlGameScore expected, ArmlGameScore actual) {
        assertThat(expected)
            .as("Verify ArmlGameScore auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArmlGameScoreUpdatableFieldsEquals(ArmlGameScore expected, ArmlGameScore actual) {
        assertThat(expected)
            .as("Verify ArmlGameScore relevant properties")
            .satisfies(e -> assertThat(e.getScore()).as("check score").isEqualTo(actual.getScore()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArmlGameScoreUpdatableRelationshipsEquals(ArmlGameScore expected, ArmlGameScore actual) {
        assertThat(expected)
            .as("Verify ArmlGameScore relationships")
            .satisfies(e -> assertThat(e.getArmlGame()).as("check armlGame").isEqualTo(actual.getArmlGame()))
            .satisfies(e -> assertThat(e.getArmlPlayer()).as("check armlPlayer").isEqualTo(actual.getArmlPlayer()));
    }
}
