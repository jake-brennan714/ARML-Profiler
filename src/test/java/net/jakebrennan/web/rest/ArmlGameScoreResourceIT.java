package net.jakebrennan.web.rest;

import static net.jakebrennan.domain.ArmlGameScoreAsserts.*;
import static net.jakebrennan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import net.jakebrennan.IntegrationTest;
import net.jakebrennan.domain.ArmlGameScore;
import net.jakebrennan.repository.ArmlGameScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArmlGameScoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArmlGameScoreResourceIT {

    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    private static final String ENTITY_API_URL = "/api/arml-game-scores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArmlGameScoreRepository armlGameScoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArmlGameScoreMockMvc;

    private ArmlGameScore armlGameScore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlGameScore createEntity(EntityManager em) {
        ArmlGameScore armlGameScore = new ArmlGameScore().score(DEFAULT_SCORE);
        return armlGameScore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlGameScore createUpdatedEntity(EntityManager em) {
        ArmlGameScore armlGameScore = new ArmlGameScore().score(UPDATED_SCORE);
        return armlGameScore;
    }

    @BeforeEach
    public void initTest() {
        armlGameScore = createEntity(em);
    }

    @Test
    @Transactional
    void createArmlGameScore() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArmlGameScore
        var returnedArmlGameScore = om.readValue(
            restArmlGameScoreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGameScore)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArmlGameScore.class
        );

        // Validate the ArmlGameScore in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertArmlGameScoreUpdatableFieldsEquals(returnedArmlGameScore, getPersistedArmlGameScore(returnedArmlGameScore));
    }

    @Test
    @Transactional
    void createArmlGameScoreWithExistingId() throws Exception {
        // Create the ArmlGameScore with an existing ID
        armlGameScore.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArmlGameScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGameScore)))
            .andExpect(status().isBadRequest());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScoreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        armlGameScore.setScore(null);

        // Create the ArmlGameScore, which fails.

        restArmlGameScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGameScore)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArmlGameScores() throws Exception {
        // Initialize the database
        armlGameScoreRepository.saveAndFlush(armlGameScore);

        // Get all the armlGameScoreList
        restArmlGameScoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(armlGameScore.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())));
    }

    @Test
    @Transactional
    void getArmlGameScore() throws Exception {
        // Initialize the database
        armlGameScoreRepository.saveAndFlush(armlGameScore);

        // Get the armlGameScore
        restArmlGameScoreMockMvc
            .perform(get(ENTITY_API_URL_ID, armlGameScore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(armlGameScore.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingArmlGameScore() throws Exception {
        // Get the armlGameScore
        restArmlGameScoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArmlGameScore() throws Exception {
        // Initialize the database
        armlGameScoreRepository.saveAndFlush(armlGameScore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlGameScore
        ArmlGameScore updatedArmlGameScore = armlGameScoreRepository.findById(armlGameScore.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArmlGameScore are not directly saved in db
        em.detach(updatedArmlGameScore);
        updatedArmlGameScore.score(UPDATED_SCORE);

        restArmlGameScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArmlGameScore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedArmlGameScore))
            )
            .andExpect(status().isOk());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArmlGameScoreToMatchAllProperties(updatedArmlGameScore);
    }

    @Test
    @Transactional
    void putNonExistingArmlGameScore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGameScore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlGameScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, armlGameScore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(armlGameScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArmlGameScore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGameScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(armlGameScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArmlGameScore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGameScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameScoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGameScore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArmlGameScoreWithPatch() throws Exception {
        // Initialize the database
        armlGameScoreRepository.saveAndFlush(armlGameScore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlGameScore using partial update
        ArmlGameScore partialUpdatedArmlGameScore = new ArmlGameScore();
        partialUpdatedArmlGameScore.setId(armlGameScore.getId());

        partialUpdatedArmlGameScore.score(UPDATED_SCORE);

        restArmlGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlGameScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlGameScore))
            )
            .andExpect(status().isOk());

        // Validate the ArmlGameScore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlGameScoreUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArmlGameScore, armlGameScore),
            getPersistedArmlGameScore(armlGameScore)
        );
    }

    @Test
    @Transactional
    void fullUpdateArmlGameScoreWithPatch() throws Exception {
        // Initialize the database
        armlGameScoreRepository.saveAndFlush(armlGameScore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlGameScore using partial update
        ArmlGameScore partialUpdatedArmlGameScore = new ArmlGameScore();
        partialUpdatedArmlGameScore.setId(armlGameScore.getId());

        partialUpdatedArmlGameScore.score(UPDATED_SCORE);

        restArmlGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlGameScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlGameScore))
            )
            .andExpect(status().isOk());

        // Validate the ArmlGameScore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlGameScoreUpdatableFieldsEquals(partialUpdatedArmlGameScore, getPersistedArmlGameScore(partialUpdatedArmlGameScore));
    }

    @Test
    @Transactional
    void patchNonExistingArmlGameScore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGameScore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, armlGameScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlGameScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArmlGameScore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGameScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlGameScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArmlGameScore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGameScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameScoreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(armlGameScore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlGameScore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArmlGameScore() throws Exception {
        // Initialize the database
        armlGameScoreRepository.saveAndFlush(armlGameScore);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the armlGameScore
        restArmlGameScoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, armlGameScore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return armlGameScoreRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ArmlGameScore getPersistedArmlGameScore(ArmlGameScore armlGameScore) {
        return armlGameScoreRepository.findById(armlGameScore.getId()).orElseThrow();
    }

    protected void assertPersistedArmlGameScoreToMatchAllProperties(ArmlGameScore expectedArmlGameScore) {
        assertArmlGameScoreAllPropertiesEquals(expectedArmlGameScore, getPersistedArmlGameScore(expectedArmlGameScore));
    }

    protected void assertPersistedArmlGameScoreToMatchUpdatableProperties(ArmlGameScore expectedArmlGameScore) {
        assertArmlGameScoreAllUpdatablePropertiesEquals(expectedArmlGameScore, getPersistedArmlGameScore(expectedArmlGameScore));
    }
}
