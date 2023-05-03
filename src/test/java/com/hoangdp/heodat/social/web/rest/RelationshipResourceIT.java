package com.hoangdp.heodat.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hoangdp.heodat.social.IntegrationTest;
import com.hoangdp.heodat.social.domain.Relationship;
import com.hoangdp.heodat.social.repository.RelationshipRepository;
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
 * Integration tests for the {@link RelationshipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RelationshipResourceIT {

    private static final Long DEFAULT_OWNER = 1L;
    private static final Long UPDATED_OWNER = 2L;

    private static final Long DEFAULT_PARTNER = 1L;
    private static final Long UPDATED_PARTNER = 2L;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{relationshipId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelationshipMockMvc;

    private Relationship relationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relationship createEntity(EntityManager em) {
        Relationship relationship = new Relationship()
            .owner(DEFAULT_OWNER)
            .partner(DEFAULT_PARTNER)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        return relationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relationship createUpdatedEntity(EntityManager em) {
        Relationship relationship = new Relationship()
            .owner(UPDATED_OWNER)
            .partner(UPDATED_PARTNER)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        return relationship;
    }

    @BeforeEach
    public void initTest() {
        relationship = createEntity(em);
    }

    @Test
    @Transactional
    void createRelationship() throws Exception {
        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();
        // Create the Relationship
        restRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isCreated());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate + 1);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testRelationship.getPartner()).isEqualTo(DEFAULT_PARTNER);
        assertThat(testRelationship.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRelationship.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRelationship.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRelationship.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testRelationship.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
    }

    @Test
    @Transactional
    void createRelationshipWithExistingId() throws Exception {
        // Create the Relationship with an existing ID
        relationship.setRelationshipId(1L);

        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRelationships() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=relationshipId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].relationshipId").value(hasItem(relationship.getRelationshipId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.intValue())))
            .andExpect(jsonPath("$.[*].partner").value(hasItem(DEFAULT_PARTNER.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }

    @Test
    @Transactional
    void getRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get the relationship
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, relationship.getRelationshipId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.relationshipId").value(relationship.getRelationshipId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.intValue()))
            .andExpect(jsonPath("$.partner").value(DEFAULT_PARTNER.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRelationship() throws Exception {
        // Get the relationship
        restRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship
        Relationship updatedRelationship = relationshipRepository.findById(relationship.getRelationshipId()).get();
        // Disconnect from session so that the updates on updatedRelationship are not directly saved in db
        em.detach(updatedRelationship);
        updatedRelationship
            .owner(UPDATED_OWNER)
            .partner(UPDATED_PARTNER)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);

        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRelationship.getRelationshipId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testRelationship.getPartner()).isEqualTo(UPDATED_PARTNER);
        assertThat(testRelationship.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRelationship.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRelationship.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRelationship.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testRelationship.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    void putNonExistingRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setRelationshipId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relationship.getRelationshipId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setRelationshipId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setRelationshipId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelationshipWithPatch() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship using partial update
        Relationship partialUpdatedRelationship = new Relationship();
        partialUpdatedRelationship.setRelationshipId(relationship.getRelationshipId());

        partialUpdatedRelationship
            .owner(UPDATED_OWNER)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);

        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelationship.getRelationshipId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testRelationship.getPartner()).isEqualTo(DEFAULT_PARTNER);
        assertThat(testRelationship.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRelationship.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRelationship.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRelationship.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testRelationship.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    void fullUpdateRelationshipWithPatch() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship using partial update
        Relationship partialUpdatedRelationship = new Relationship();
        partialUpdatedRelationship.setRelationshipId(relationship.getRelationshipId());

        partialUpdatedRelationship
            .owner(UPDATED_OWNER)
            .partner(UPDATED_PARTNER)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);

        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelationship.getRelationshipId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testRelationship.getPartner()).isEqualTo(UPDATED_PARTNER);
        assertThat(testRelationship.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRelationship.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRelationship.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRelationship.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testRelationship.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setRelationshipId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relationship.getRelationshipId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setRelationshipId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setRelationshipId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeDelete = relationshipRepository.findAll().size();

        // Delete the relationship
        restRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, relationship.getRelationshipId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
