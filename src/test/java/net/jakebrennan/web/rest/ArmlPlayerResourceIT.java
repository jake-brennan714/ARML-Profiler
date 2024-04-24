package net.jakebrennan.web.rest;

import static net.jakebrennan.domain.ArmlPlayerAsserts.*;
import static net.jakebrennan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import net.jakebrennan.IntegrationTest;
import net.jakebrennan.domain.ArmlPlayer;
import net.jakebrennan.domain.enumeration.ArmlLeague;
import net.jakebrennan.repository.ArmlPlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArmlPlayerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ArmlPlayerResourceIT {

    private static final Long DEFAULT_PLAYER_ID = 1L;
    private static final Long UPDATED_PLAYER_ID = 2L;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TENHOU_NAME = "AAAAAAAA";
    private static final String UPDATED_TENHOU_NAME = "BBBBBBBB";

    private static final ArmlLeague DEFAULT_LEAGUE = ArmlLeague.A1;
    private static final ArmlLeague UPDATED_LEAGUE = ArmlLeague.A2;

    private static final String ENTITY_API_URL = "/api/arml-players";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArmlPlayerRepository armlPlayerRepository;

    @Mock
    private ArmlPlayerRepository armlPlayerRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArmlPlayerMockMvc;

    private ArmlPlayer armlPlayer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlPlayer createEntity(EntityManager em) {
        ArmlPlayer armlPlayer = new ArmlPlayer()
            .playerID(DEFAULT_PLAYER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .tenhouName(DEFAULT_TENHOU_NAME)
            .league(DEFAULT_LEAGUE);
        return armlPlayer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlPlayer createUpdatedEntity(EntityManager em) {
        ArmlPlayer armlPlayer = new ArmlPlayer()
            .playerID(UPDATED_PLAYER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .tenhouName(UPDATED_TENHOU_NAME)
            .league(UPDATED_LEAGUE);
        return armlPlayer;
    }

    @BeforeEach
    public void initTest() {
        armlPlayer = createEntity(em);
    }

    @Test
    @Transactional
    void createArmlPlayer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArmlPlayer
        var returnedArmlPlayer = om.readValue(
            restArmlPlayerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArmlPlayer.class
        );

        // Validate the ArmlPlayer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertArmlPlayerUpdatableFieldsEquals(returnedArmlPlayer, getPersistedArmlPlayer(returnedArmlPlayer));
    }

    @Test
    @Transactional
    void createArmlPlayerWithExistingId() throws Exception {
        // Create the ArmlPlayer with an existing ID
        armlPlayer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArmlPlayerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer)))
            .andExpect(status().isBadRequest());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlayerIDIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        armlPlayer.setPlayerID(null);

        // Create the ArmlPlayer, which fails.

        restArmlPlayerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        armlPlayer.setFirstName(null);

        // Create the ArmlPlayer, which fails.

        restArmlPlayerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        armlPlayer.setLastName(null);

        // Create the ArmlPlayer, which fails.

        restArmlPlayerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeagueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        armlPlayer.setLeague(null);

        // Create the ArmlPlayer, which fails.

        restArmlPlayerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArmlPlayers() throws Exception {
        // Initialize the database
        armlPlayerRepository.saveAndFlush(armlPlayer);

        // Get all the armlPlayerList
        restArmlPlayerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(armlPlayer.getId().intValue())))
            .andExpect(jsonPath("$.[*].playerID").value(hasItem(DEFAULT_PLAYER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].tenhouName").value(hasItem(DEFAULT_TENHOU_NAME)))
            .andExpect(jsonPath("$.[*].league").value(hasItem(DEFAULT_LEAGUE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArmlPlayersWithEagerRelationshipsIsEnabled() throws Exception {
        when(armlPlayerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArmlPlayerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(armlPlayerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArmlPlayersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(armlPlayerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArmlPlayerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(armlPlayerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getArmlPlayer() throws Exception {
        // Initialize the database
        armlPlayerRepository.saveAndFlush(armlPlayer);

        // Get the armlPlayer
        restArmlPlayerMockMvc
            .perform(get(ENTITY_API_URL_ID, armlPlayer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(armlPlayer.getId().intValue()))
            .andExpect(jsonPath("$.playerID").value(DEFAULT_PLAYER_ID.intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.tenhouName").value(DEFAULT_TENHOU_NAME))
            .andExpect(jsonPath("$.league").value(DEFAULT_LEAGUE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArmlPlayer() throws Exception {
        // Get the armlPlayer
        restArmlPlayerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArmlPlayer() throws Exception {
        // Initialize the database
        armlPlayerRepository.saveAndFlush(armlPlayer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlPlayer
        ArmlPlayer updatedArmlPlayer = armlPlayerRepository.findById(armlPlayer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArmlPlayer are not directly saved in db
        em.detach(updatedArmlPlayer);
        updatedArmlPlayer
            .playerID(UPDATED_PLAYER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .tenhouName(UPDATED_TENHOU_NAME)
            .league(UPDATED_LEAGUE);

        restArmlPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArmlPlayer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedArmlPlayer))
            )
            .andExpect(status().isOk());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArmlPlayerToMatchAllProperties(updatedArmlPlayer);
    }

    @Test
    @Transactional
    void putNonExistingArmlPlayer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlPlayer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, armlPlayer.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArmlPlayer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlPlayer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(armlPlayer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArmlPlayer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlPlayer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlPlayerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlPlayer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArmlPlayerWithPatch() throws Exception {
        // Initialize the database
        armlPlayerRepository.saveAndFlush(armlPlayer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlPlayer using partial update
        ArmlPlayer partialUpdatedArmlPlayer = new ArmlPlayer();
        partialUpdatedArmlPlayer.setId(armlPlayer.getId());

        partialUpdatedArmlPlayer.playerID(UPDATED_PLAYER_ID).league(UPDATED_LEAGUE);

        restArmlPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlPlayer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlPlayer))
            )
            .andExpect(status().isOk());

        // Validate the ArmlPlayer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlPlayerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArmlPlayer, armlPlayer),
            getPersistedArmlPlayer(armlPlayer)
        );
    }

    @Test
    @Transactional
    void fullUpdateArmlPlayerWithPatch() throws Exception {
        // Initialize the database
        armlPlayerRepository.saveAndFlush(armlPlayer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlPlayer using partial update
        ArmlPlayer partialUpdatedArmlPlayer = new ArmlPlayer();
        partialUpdatedArmlPlayer.setId(armlPlayer.getId());

        partialUpdatedArmlPlayer
            .playerID(UPDATED_PLAYER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .tenhouName(UPDATED_TENHOU_NAME)
            .league(UPDATED_LEAGUE);

        restArmlPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlPlayer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlPlayer))
            )
            .andExpect(status().isOk());

        // Validate the ArmlPlayer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlPlayerUpdatableFieldsEquals(partialUpdatedArmlPlayer, getPersistedArmlPlayer(partialUpdatedArmlPlayer));
    }

    @Test
    @Transactional
    void patchNonExistingArmlPlayer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlPlayer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, armlPlayer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlPlayer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArmlPlayer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlPlayer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlPlayer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArmlPlayer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlPlayer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlPlayerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(armlPlayer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlPlayer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArmlPlayer() throws Exception {
        // Initialize the database
        armlPlayerRepository.saveAndFlush(armlPlayer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the armlPlayer
        restArmlPlayerMockMvc
            .perform(delete(ENTITY_API_URL_ID, armlPlayer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return armlPlayerRepository.count();
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

    protected ArmlPlayer getPersistedArmlPlayer(ArmlPlayer armlPlayer) {
        return armlPlayerRepository.findById(armlPlayer.getId()).orElseThrow();
    }

    protected void assertPersistedArmlPlayerToMatchAllProperties(ArmlPlayer expectedArmlPlayer) {
        assertArmlPlayerAllPropertiesEquals(expectedArmlPlayer, getPersistedArmlPlayer(expectedArmlPlayer));
    }

    protected void assertPersistedArmlPlayerToMatchUpdatableProperties(ArmlPlayer expectedArmlPlayer) {
        assertArmlPlayerAllUpdatablePropertiesEquals(expectedArmlPlayer, getPersistedArmlPlayer(expectedArmlPlayer));
    }
}
