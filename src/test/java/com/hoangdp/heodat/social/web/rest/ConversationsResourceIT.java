package com.hoangdp.heodat.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hoangdp.heodat.social.IntegrationTest;
import com.hoangdp.heodat.social.domain.Conversations;
import com.hoangdp.heodat.social.repository.ConversationsRepository;
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
 * Integration tests for the {@link ConversationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConversationsResourceIT {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_SENDER = 1L;
    private static final Long UPDATED_SENDER = 2L;

    private static final Long DEFAULT_RECEIVER = 1L;
    private static final Long UPDATED_RECEIVER = 2L;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conversations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{conversationId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConversationsRepository conversationsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConversationsMockMvc;

    private Conversations conversations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversations createEntity(EntityManager em) {
        Conversations conversations = new Conversations()
            .timestamp(DEFAULT_TIMESTAMP)
            .sender(DEFAULT_SENDER)
            .receiver(DEFAULT_RECEIVER)
            .message(DEFAULT_MESSAGE);
        return conversations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversations createUpdatedEntity(EntityManager em) {
        Conversations conversations = new Conversations()
            .timestamp(UPDATED_TIMESTAMP)
            .sender(UPDATED_SENDER)
            .receiver(UPDATED_RECEIVER)
            .message(UPDATED_MESSAGE);
        return conversations;
    }

    @BeforeEach
    public void initTest() {
        conversations = createEntity(em);
    }

    @Test
    @Transactional
    void createConversations() throws Exception {
        int databaseSizeBeforeCreate = conversationsRepository.findAll().size();
        // Create the Conversations
        restConversationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversations)))
            .andExpect(status().isCreated());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeCreate + 1);
        Conversations testConversations = conversationsList.get(conversationsList.size() - 1);
        assertThat(testConversations.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testConversations.getSender()).isEqualTo(DEFAULT_SENDER);
        assertThat(testConversations.getReceiver()).isEqualTo(DEFAULT_RECEIVER);
        assertThat(testConversations.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void createConversationsWithExistingId() throws Exception {
        // Create the Conversations with an existing ID
        conversations.setConversationId(1L);

        int databaseSizeBeforeCreate = conversationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConversationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversations)))
            .andExpect(status().isBadRequest());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = conversationsRepository.findAll().size();
        // set the field null
        conversations.setTimestamp(null);

        // Create the Conversations, which fails.

        restConversationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversations)))
            .andExpect(status().isBadRequest());

        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = conversationsRepository.findAll().size();
        // set the field null
        conversations.setSender(null);

        // Create the Conversations, which fails.

        restConversationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversations)))
            .andExpect(status().isBadRequest());

        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceiverIsRequired() throws Exception {
        int databaseSizeBeforeTest = conversationsRepository.findAll().size();
        // set the field null
        conversations.setReceiver(null);

        // Create the Conversations, which fails.

        restConversationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversations)))
            .andExpect(status().isBadRequest());

        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = conversationsRepository.findAll().size();
        // set the field null
        conversations.setMessage(null);

        // Create the Conversations, which fails.

        restConversationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversations)))
            .andExpect(status().isBadRequest());

        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConversations() throws Exception {
        // Initialize the database
        conversationsRepository.saveAndFlush(conversations);

        // Get all the conversationsList
        restConversationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=conversationId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].conversationId").value(hasItem(conversations.getConversationId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER.intValue())))
            .andExpect(jsonPath("$.[*].receiver").value(hasItem(DEFAULT_RECEIVER.intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @Test
    @Transactional
    void getConversations() throws Exception {
        // Initialize the database
        conversationsRepository.saveAndFlush(conversations);

        // Get the conversations
        restConversationsMockMvc
            .perform(get(ENTITY_API_URL_ID, conversations.getConversationId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.conversationId").value(conversations.getConversationId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.sender").value(DEFAULT_SENDER.intValue()))
            .andExpect(jsonPath("$.receiver").value(DEFAULT_RECEIVER.intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingConversations() throws Exception {
        // Get the conversations
        restConversationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConversations() throws Exception {
        // Initialize the database
        conversationsRepository.saveAndFlush(conversations);

        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();

        // Update the conversations
        Conversations updatedConversations = conversationsRepository.findById(conversations.getConversationId()).get();
        // Disconnect from session so that the updates on updatedConversations are not directly saved in db
        em.detach(updatedConversations);
        updatedConversations.timestamp(UPDATED_TIMESTAMP).sender(UPDATED_SENDER).receiver(UPDATED_RECEIVER).message(UPDATED_MESSAGE);

        restConversationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConversations.getConversationId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConversations))
            )
            .andExpect(status().isOk());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
        Conversations testConversations = conversationsList.get(conversationsList.size() - 1);
        assertThat(testConversations.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testConversations.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testConversations.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testConversations.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingConversations() throws Exception {
        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();
        conversations.setConversationId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversations.getConversationId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConversations() throws Exception {
        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();
        conversations.setConversationId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConversations() throws Exception {
        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();
        conversations.setConversationId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversations)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConversationsWithPatch() throws Exception {
        // Initialize the database
        conversationsRepository.saveAndFlush(conversations);

        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();

        // Update the conversations using partial update
        Conversations partialUpdatedConversations = new Conversations();
        partialUpdatedConversations.setConversationId(conversations.getConversationId());

        partialUpdatedConversations.sender(UPDATED_SENDER).receiver(UPDATED_RECEIVER);

        restConversationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversations.getConversationId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversations))
            )
            .andExpect(status().isOk());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
        Conversations testConversations = conversationsList.get(conversationsList.size() - 1);
        assertThat(testConversations.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testConversations.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testConversations.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testConversations.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateConversationsWithPatch() throws Exception {
        // Initialize the database
        conversationsRepository.saveAndFlush(conversations);

        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();

        // Update the conversations using partial update
        Conversations partialUpdatedConversations = new Conversations();
        partialUpdatedConversations.setConversationId(conversations.getConversationId());

        partialUpdatedConversations.timestamp(UPDATED_TIMESTAMP).sender(UPDATED_SENDER).receiver(UPDATED_RECEIVER).message(UPDATED_MESSAGE);

        restConversationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversations.getConversationId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversations))
            )
            .andExpect(status().isOk());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
        Conversations testConversations = conversationsList.get(conversationsList.size() - 1);
        assertThat(testConversations.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testConversations.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testConversations.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testConversations.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingConversations() throws Exception {
        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();
        conversations.setConversationId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conversations.getConversationId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConversations() throws Exception {
        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();
        conversations.setConversationId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConversations() throws Exception {
        int databaseSizeBeforeUpdate = conversationsRepository.findAll().size();
        conversations.setConversationId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conversations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conversations in the database
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConversations() throws Exception {
        // Initialize the database
        conversationsRepository.saveAndFlush(conversations);

        int databaseSizeBeforeDelete = conversationsRepository.findAll().size();

        // Delete the conversations
        restConversationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, conversations.getConversationId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conversations> conversationsList = conversationsRepository.findAll();
        assertThat(conversationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
