package net.jakebrennan.web.rest;

import static net.jakebrennan.domain.ArmlProfileAsserts.*;
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
import net.jakebrennan.domain.ArmlProfile;
import net.jakebrennan.repository.ArmlProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArmlProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArmlProfileResourceIT {

    private static final Double DEFAULT_WIN_RATE = 0.0D;
    private static final Double UPDATED_WIN_RATE = 1D;

    private static final Double DEFAULT_FEED_RATE = 0.0D;
    private static final Double UPDATED_FEED_RATE = 1D;

    private static final Double DEFAULT_CALL_RATE = 0.0D;
    private static final Double UPDATED_CALL_RATE = 1D;

    private static final Double DEFAULT_RII_RATE = 0.0D;
    private static final Double UPDATED_RII_RATE = 1D;

    private static final Long DEFAULT_FEED_EV = 1L;
    private static final Long UPDATED_FEED_EV = 2L;

    private static final String ENTITY_API_URL = "/api/arml-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArmlProfileRepository armlProfileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArmlProfileMockMvc;

    private ArmlProfile armlProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlProfile createEntity(EntityManager em) {
        ArmlProfile armlProfile = new ArmlProfile()
            .winRate(DEFAULT_WIN_RATE)
            .feedRate(DEFAULT_FEED_RATE)
            .callRate(DEFAULT_CALL_RATE)
            .riiRate(DEFAULT_RII_RATE)
            .feedEV(DEFAULT_FEED_EV);
        return armlProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmlProfile createUpdatedEntity(EntityManager em) {
        ArmlProfile armlProfile = new ArmlProfile()
            .winRate(UPDATED_WIN_RATE)
            .feedRate(UPDATED_FEED_RATE)
            .callRate(UPDATED_CALL_RATE)
            .riiRate(UPDATED_RII_RATE)
            .feedEV(UPDATED_FEED_EV);
        return armlProfile;
    }

    @BeforeEach
    public void initTest() {
        armlProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createArmlProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArmlProfile
        var returnedArmlProfile = om.readValue(
            restArmlProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlProfile)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArmlProfile.class
        );

        // Validate the ArmlProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertArmlProfileUpdatableFieldsEquals(returnedArmlProfile, getPersistedArmlProfile(returnedArmlProfile));
    }

    @Test
    @Transactional
    void createArmlProfileWithExistingId() throws Exception {
        // Create the ArmlProfile with an existing ID
        armlProfile.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArmlProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlProfile)))
            .andExpect(status().isBadRequest());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArmlProfiles() throws Exception {
        // Initialize the database
        armlProfileRepository.saveAndFlush(armlProfile);

        // Get all the armlProfileList
        restArmlProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(armlProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].winRate").value(hasItem(DEFAULT_WIN_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].feedRate").value(hasItem(DEFAULT_FEED_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].callRate").value(hasItem(DEFAULT_CALL_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].riiRate").value(hasItem(DEFAULT_RII_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].feedEV").value(hasItem(DEFAULT_FEED_EV.intValue())));
    }

    @Test
    @Transactional
    void getArmlProfile() throws Exception {
        // Initialize the database
        armlProfileRepository.saveAndFlush(armlProfile);

        // Get the armlProfile
        restArmlProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, armlProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(armlProfile.getId().intValue()))
            .andExpect(jsonPath("$.winRate").value(DEFAULT_WIN_RATE.doubleValue()))
            .andExpect(jsonPath("$.feedRate").value(DEFAULT_FEED_RATE.doubleValue()))
            .andExpect(jsonPath("$.callRate").value(DEFAULT_CALL_RATE.doubleValue()))
            .andExpect(jsonPath("$.riiRate").value(DEFAULT_RII_RATE.doubleValue()))
            .andExpect(jsonPath("$.feedEV").value(DEFAULT_FEED_EV.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingArmlProfile() throws Exception {
        // Get the armlProfile
        restArmlProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArmlProfile() throws Exception {
        // Initialize the database
        armlProfileRepository.saveAndFlush(armlProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlProfile
        ArmlProfile updatedArmlProfile = armlProfileRepository.findById(armlProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArmlProfile are not directly saved in db
        em.detach(updatedArmlProfile);
        updatedArmlProfile
            .winRate(UPDATED_WIN_RATE)
            .feedRate(UPDATED_FEED_RATE)
            .callRate(UPDATED_CALL_RATE)
            .riiRate(UPDATED_RII_RATE)
            .feedEV(UPDATED_FEED_EV);

        restArmlProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArmlProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedArmlProfile))
            )
            .andExpect(status().isOk());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArmlProfileToMatchAllProperties(updatedArmlProfile);
    }

    @Test
    @Transactional
    void putNonExistingArmlProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, armlProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(armlProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArmlProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(armlProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArmlProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(armlProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArmlProfileWithPatch() throws Exception {
        // Initialize the database
        armlProfileRepository.saveAndFlush(armlProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlProfile using partial update
        ArmlProfile partialUpdatedArmlProfile = new ArmlProfile();
        partialUpdatedArmlProfile.setId(armlProfile.getId());

        partialUpdatedArmlProfile.feedRate(UPDATED_FEED_RATE).callRate(UPDATED_CALL_RATE).riiRate(UPDATED_RII_RATE).feedEV(UPDATED_FEED_EV);

        restArmlProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlProfile))
            )
            .andExpect(status().isOk());

        // Validate the ArmlProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArmlProfile, armlProfile),
            getPersistedArmlProfile(armlProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateArmlProfileWithPatch() throws Exception {
        // Initialize the database
        armlProfileRepository.saveAndFlush(armlProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the armlProfile using partial update
        ArmlProfile partialUpdatedArmlProfile = new ArmlProfile();
        partialUpdatedArmlProfile.setId(armlProfile.getId());

        partialUpdatedArmlProfile
            .winRate(UPDATED_WIN_RATE)
            .feedRate(UPDATED_FEED_RATE)
            .callRate(UPDATED_CALL_RATE)
            .riiRate(UPDATED_RII_RATE)
            .feedEV(UPDATED_FEED_EV);

        restArmlProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmlProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArmlProfile))
            )
            .andExpect(status().isOk());

        // Validate the ArmlProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArmlProfileUpdatableFieldsEquals(partialUpdatedArmlProfile, getPersistedArmlProfile(partialUpdatedArmlProfile));
    }

    @Test
    @Transactional
    void patchNonExistingArmlProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmlProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, armlProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArmlProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(armlProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArmlProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        armlProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmlProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(armlProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmlProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArmlProfile() throws Exception {
        // Initialize the database
        armlProfileRepository.saveAndFlush(armlProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the armlProfile
        restArmlProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, armlProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return armlProfileRepository.count();
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

    protected ArmlProfile getPersistedArmlProfile(ArmlProfile armlProfile) {
        return armlProfileRepository.findById(armlProfile.getId()).orElseThrow();
    }

    protected void assertPersistedArmlProfileToMatchAllProperties(ArmlProfile expectedArmlProfile) {
        assertArmlProfileAllPropertiesEquals(expectedArmlProfile, getPersistedArmlProfile(expectedArmlProfile));
    }

    protected void assertPersistedArmlProfileToMatchUpdatableProperties(ArmlProfile expectedArmlProfile) {
        assertArmlProfileAllUpdatablePropertiesEquals(expectedArmlProfile, getPersistedArmlProfile(expectedArmlProfile));
    }
}
