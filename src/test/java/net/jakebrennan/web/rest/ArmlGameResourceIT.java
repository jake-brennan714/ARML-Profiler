package net.jakebrennan.web.rest;

import static net.jakebrennan.domain.ArmlGameAsserts.*;
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
import net.jakebrennan.domain.ArmlGame;
import net.jakebrennan.repository.ArmlGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArmlGameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArmlGameResourceIT {

    private static final Long DEFAULT_GAME_ID = 1L;
    private static final Long UPDATED_GAME_ID = 2L;

    private static final String ENTITY_API_URL = "/api/arml-games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArmlGameRepository armlGameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArmlGameMockMvc;

    private ArmlGame armlGame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlGame createEntity(EntityManager em) {
        ArmlGame armlGame = new ArmlGame().gameID(DEFAULT_GAME_ID);
        return armlGame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlGame createUpdatedEntity(EntityManager em) {
        ArmlGame armlGame = new ArmlGame().gameID(UPDATED_GAME_ID);
        return armlGame;
    }

    @BeforeEach
    public void initTest() {
        armlGame = createEntity(em);
    }

    @Test
    @Transactional
    void createArmlGame() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArmlGame
        var returnedArmlGame = om.readValue(
            restArmlGameMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGame)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArmlGame.class
        );

        // Validate the ArmlGame in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertArmlGameUpdatableFieldsEquals(returnedArmlGame, getPersistedArmlGame(returnedArmlGame));
    }

    @Test
    @Transactional
    void createArmlGameWithExistingId() throws Exception {
        // Create the ArmlGame with an existing ID
        armlGame.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArmlGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGame)))
            .andExpect(status().isBadRequest());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGameIDIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        armlGame.setGameID(null);

        // Create the ArmlGame, which fails.

        restArmlGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGame)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArmlGames() throws Exception {
        // Initialize the database
        armlGameRepository.saveAndFlush(armlGame);

        // Get all the armlGameList
        restArmlGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(armlGame.getId().intValue())))
            .andExpect(jsonPath("$.[*].gameID").value(hasItem(DEFAULT_GAME_ID.intValue())));
    }

    @Test
    @Transactional
    void getArmlGame() throws Exception {
        // Initialize the database
        armlGameRepository.saveAndFlush(armlGame);

        // Get the armlGame
        restArmlGameMockMvc
            .perform(get(ENTITY_API_URL_ID, armlGame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(armlGame.getId().intValue()))
            .andExpect(jsonPath("$.gameID").value(DEFAULT_GAME_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingArmlGame() throws Exception {
        // Get the armlGame
        restArmlGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArmlGame() throws Exception {
        // Initialize the database
        armlGameRepository.saveAndFlush(armlGame);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlGame
        ArmlGame updatedArmlGame = armlGameRepository.findById(armlGame.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArmlGame are not directly saved in db
        em.detach(updatedArmlGame);
        updatedArmlGame.gameID(UPDATED_GAME_ID);

        restArmlGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArmlGame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedArmlGame))
            )
            .andExpect(status().isOk());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArmlGameToMatchAllProperties(updatedArmlGame);
    }

    @Test
    @Transactional
    void putNonExistingArmlGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGame.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, armlGame.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArmlGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGame.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(armlGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArmlGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGame.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlGame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArmlGameWithPatch() throws Exception {
        // Initialize the database
        armlGameRepository.saveAndFlush(armlGame);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlGame using partial update
        ArmlGame partialUpdatedArmlGame = new ArmlGame();
        partialUpdatedArmlGame.setId(armlGame.getId());

        partialUpdatedArmlGame.gameID(UPDATED_GAME_ID);

        restArmlGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlGame))
            )
            .andExpect(status().isOk());

        // Validate the ArmlGame in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlGameUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedArmlGame, armlGame), getPersistedArmlGame(armlGame));
    }

    @Test
    @Transactional
    void fullUpdateArmlGameWithPatch() throws Exception {
        // Initialize the database
        armlGameRepository.saveAndFlush(armlGame);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlGame using partial update
        ArmlGame partialUpdatedArmlGame = new ArmlGame();
        partialUpdatedArmlGame.setId(armlGame.getId());

        partialUpdatedArmlGame.gameID(UPDATED_GAME_ID);

        restArmlGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlGame))
            )
            .andExpect(status().isOk());

        // Validate the ArmlGame in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlGameUpdatableFieldsEquals(partialUpdatedArmlGame, getPersistedArmlGame(partialUpdatedArmlGame));
    }

    @Test
    @Transactional
    void patchNonExistingArmlGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGame.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, armlGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArmlGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGame.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArmlGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlGame.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlGameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(armlGame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArmlGame() throws Exception {
        // Initialize the database
        armlGameRepository.saveAndFlush(armlGame);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the armlGame
        restArmlGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, armlGame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return armlGameRepository.count();
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

    protected ArmlGame getPersistedArmlGame(ArmlGame armlGame) {
        return armlGameRepository.findById(armlGame.getId()).orElseThrow();
    }

    protected void assertPersistedArmlGameToMatchAllProperties(ArmlGame expectedArmlGame) {
        assertArmlGameAllPropertiesEquals(expectedArmlGame, getPersistedArmlGame(expectedArmlGame));
    }

    protected void assertPersistedArmlGameToMatchUpdatableProperties(ArmlGame expectedArmlGame) {
        assertArmlGameAllUpdatablePropertiesEquals(expectedArmlGame, getPersistedArmlGame(expectedArmlGame));
    }
}
