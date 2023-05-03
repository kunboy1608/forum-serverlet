package com.hoangdp.heodat.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hoangdp.heodat.social.IntegrationTest;
import com.hoangdp.heodat.social.domain.ConversationsDetails;
import com.hoangdp.heodat.social.repository.ConversationsDetailsRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConversationsDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConversationsDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_GROUP = false;
    private static final Boolean UPDATED_IS_GROUP = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/conversations-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConversationsDetailsRepository conversationsDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConversationsDetailsMockMvc;

    private ConversationsDetails conversationsDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConversationsDetails createEntity(EntityManager em) {
        ConversationsDetails conversationsDetails = new ConversationsDetails()
            .name(DEFAULT_NAME)
            .isGroup(DEFAULT_IS_GROUP)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        return conversationsDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConversationsDetails createUpdatedEntity(EntityManager em) {
        ConversationsDetails conversationsDetails = new ConversationsDetails()
            .name(UPDATED_NAME)
            .isGroup(UPDATED_IS_GROUP)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        return conversationsDetails;
    }

    @BeforeEach
    public void initTest() {
        conversationsDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createConversationsDetails() throws Exception {
        int databaseSizeBeforeCreate = conversationsDetailsRepository.findAll().size();
        // Create the ConversationsDetails
        restConversationsDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isCreated());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ConversationsDetails testConversationsDetails = conversationsDetailsList.get(conversationsDetailsList.size() - 1);
        assertThat(testConversationsDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConversationsDetails.getIsGroup()).isEqualTo(DEFAULT_IS_GROUP);
        assertThat(testConversationsDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testConversationsDetails.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testConversationsDetails.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testConversationsDetails.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
    }

    @Test
    @Transactional
    void createConversationsDetailsWithExistingId() throws Exception {
        // Create the ConversationsDetails with an existing ID
        conversationsDetails.setId(1L);

        int databaseSizeBeforeCreate = conversationsDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConversationsDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConversationsDetails() throws Exception {
        // Initialize the database
        conversationsDetailsRepository.saveAndFlush(conversationsDetails);

        // Get all the conversationsDetailsList
        restConversationsDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversationsDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isGroup").value(hasItem(DEFAULT_IS_GROUP.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }

    @Test
    @Transactional
    void getConversationsDetails() throws Exception {
        // Initialize the database
        conversationsDetailsRepository.saveAndFlush(conversationsDetails);

        // Get the conversationsDetails
        restConversationsDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, conversationsDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conversationsDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isGroup").value(DEFAULT_IS_GROUP.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingConversationsDetails() throws Exception {
        // Get the conversationsDetails
        restConversationsDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConversationsDetails() throws Exception {
        // Initialize the database
        conversationsDetailsRepository.saveAndFlush(conversationsDetails);

        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();

        // Update the conversationsDetails
        ConversationsDetails updatedConversationsDetails = conversationsDetailsRepository.findById(conversationsDetails.getId()).get();
        // Disconnect from session so that the updates on updatedConversationsDetails are not directly saved in db
        em.detach(updatedConversationsDetails);
        updatedConversationsDetails
            .name(UPDATED_NAME)
            .isGroup(UPDATED_IS_GROUP)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);

        restConversationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConversationsDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConversationsDetails))
            )
            .andExpect(status().isOk());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
        ConversationsDetails testConversationsDetails = conversationsDetailsList.get(conversationsDetailsList.size() - 1);
        assertThat(testConversationsDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConversationsDetails.getIsGroup()).isEqualTo(UPDATED_IS_GROUP);
        assertThat(testConversationsDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testConversationsDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testConversationsDetails.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testConversationsDetails.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    void putNonExistingConversationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();
        conversationsDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversationsDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConversationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();
        conversationsDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConversationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();
        conversationsDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConversationsDetailsWithPatch() throws Exception {
        // Initialize the database
        conversationsDetailsRepository.saveAndFlush(conversationsDetails);

        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();

        // Update the conversationsDetails using partial update
        ConversationsDetails partialUpdatedConversationsDetails = new ConversationsDetails();
        partialUpdatedConversationsDetails.setId(conversationsDetails.getId());

        partialUpdatedConversationsDetails.isGroup(UPDATED_IS_GROUP).createdOn(UPDATED_CREATED_ON).modifiedOn(UPDATED_MODIFIED_ON);

        restConversationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversationsDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversationsDetails))
            )
            .andExpect(status().isOk());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
        ConversationsDetails testConversationsDetails = conversationsDetailsList.get(conversationsDetailsList.size() - 1);
        assertThat(testConversationsDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConversationsDetails.getIsGroup()).isEqualTo(UPDATED_IS_GROUP);
        assertThat(testConversationsDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testConversationsDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testConversationsDetails.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testConversationsDetails.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    void fullUpdateConversationsDetailsWithPatch() throws Exception {
        // Initialize the database
        conversationsDetailsRepository.saveAndFlush(conversationsDetails);

        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();

        // Update the conversationsDetails using partial update
        ConversationsDetails partialUpdatedConversationsDetails = new ConversationsDetails();
        partialUpdatedConversationsDetails.setId(conversationsDetails.getId());

        partialUpdatedConversationsDetails
            .name(UPDATED_NAME)
            .isGroup(UPDATED_IS_GROUP)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);

        restConversationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversationsDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversationsDetails))
            )
            .andExpect(status().isOk());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
        ConversationsDetails testConversationsDetails = conversationsDetailsList.get(conversationsDetailsList.size() - 1);
        assertThat(testConversationsDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConversationsDetails.getIsGroup()).isEqualTo(UPDATED_IS_GROUP);
        assertThat(testConversationsDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testConversationsDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testConversationsDetails.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testConversationsDetails.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingConversationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();
        conversationsDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conversationsDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConversationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();
        conversationsDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConversationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = conversationsDetailsRepository.findAll().size();
        conversationsDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversationsDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConversationsDetails in the database
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConversationsDetails() throws Exception {
        // Initialize the database
        conversationsDetailsRepository.saveAndFlush(conversationsDetails);

        int databaseSizeBeforeDelete = conversationsDetailsRepository.findAll().size();

        // Delete the conversationsDetails
        restConversationsDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, conversationsDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConversationsDetails> conversationsDetailsList = conversationsDetailsRepository.findAll();
        assertThat(conversationsDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
