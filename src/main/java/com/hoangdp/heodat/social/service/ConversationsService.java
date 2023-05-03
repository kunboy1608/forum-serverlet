package com.hoangdp.heodat.social.service;

import com.hoangdp.heodat.social.domain.Conversations;
import com.hoangdp.heodat.social.repository.ConversationsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conversations}.
 */
@Service
@Transactional
public class ConversationsService {

    private final Logger log = LoggerFactory.getLogger(ConversationsService.class);

    private final ConversationsRepository conversationsRepository;

    public ConversationsService(ConversationsRepository conversationsRepository) {
        this.conversationsRepository = conversationsRepository;
    }

    /**
     * Save a conversations.
     *
     * @param conversations the entity to save.
     * @return the persisted entity.
     */
    public Conversations save(Conversations conversations) {
        log.debug("Request to save Conversations : {}", conversations);
        return conversationsRepository.save(conversations);
    }

    /**
     * Update a conversations.
     *
     * @param conversations the entity to save.
     * @return the persisted entity.
     */
    public Conversations update(Conversations conversations) {
        log.debug("Request to update Conversations : {}", conversations);
        return conversationsRepository.save(conversations);
    }

    /**
     * Partially update a conversations.
     *
     * @param conversations the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Conversations> partialUpdate(Conversations conversations) {
        log.debug("Request to partially update Conversations : {}", conversations);

        return conversationsRepository
            .findById(conversations.getConversationId())
            .map(existingConversations -> {
                if (conversations.getTimestamp() != null) {
                    existingConversations.setTimestamp(conversations.getTimestamp());
                }
                if (conversations.getSender() != null) {
                    existingConversations.setSender(conversations.getSender());
                }
                if (conversations.getReceiver() != null) {
                    existingConversations.setReceiver(conversations.getReceiver());
                }
                if (conversations.getMessage() != null) {
                    existingConversations.setMessage(conversations.getMessage());
                }

                return existingConversations;
            })
            .map(conversationsRepository::save);
    }

    /**
     * Get all the conversations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Conversations> findAll(Pageable pageable) {
        log.debug("Request to get all Conversations");
        return conversationsRepository.findAll(pageable);
    }

    /**
     * Get one conversations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Conversations> findOne(Long id) {
        log.debug("Request to get Conversations : {}", id);
        return conversationsRepository.findById(id);
    }

    /**
     * Delete the conversations by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Conversations : {}", id);
        conversationsRepository.deleteById(id);
    }
}
