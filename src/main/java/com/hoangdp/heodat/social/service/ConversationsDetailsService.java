package com.hoangdp.heodat.social.service;

import com.hoangdp.heodat.social.domain.ConversationsDetails;
import com.hoangdp.heodat.social.repository.ConversationsDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConversationsDetails}.
 */
@Service
@Transactional
public class ConversationsDetailsService {

    private final Logger log = LoggerFactory.getLogger(ConversationsDetailsService.class);

    private final ConversationsDetailsRepository conversationsDetailsRepository;

    public ConversationsDetailsService(ConversationsDetailsRepository conversationsDetailsRepository) {
        this.conversationsDetailsRepository = conversationsDetailsRepository;
    }

    /**
     * Save a conversationsDetails.
     *
     * @param conversationsDetails the entity to save.
     * @return the persisted entity.
     */
    public ConversationsDetails save(ConversationsDetails conversationsDetails) {
        log.debug("Request to save ConversationsDetails : {}", conversationsDetails);
        return conversationsDetailsRepository.save(conversationsDetails);
    }

    /**
     * Update a conversationsDetails.
     *
     * @param conversationsDetails the entity to save.
     * @return the persisted entity.
     */
    public ConversationsDetails update(ConversationsDetails conversationsDetails) {
        log.debug("Request to update ConversationsDetails : {}", conversationsDetails);
        return conversationsDetailsRepository.save(conversationsDetails);
    }

    /**
     * Partially update a conversationsDetails.
     *
     * @param conversationsDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConversationsDetails> partialUpdate(ConversationsDetails conversationsDetails) {
        log.debug("Request to partially update ConversationsDetails : {}", conversationsDetails);

        return conversationsDetailsRepository
            .findById(conversationsDetails.getId())
            .map(existingConversationsDetails -> {
                if (conversationsDetails.getName() != null) {
                    existingConversationsDetails.setName(conversationsDetails.getName());
                }
                if (conversationsDetails.getIsGroup() != null) {
                    existingConversationsDetails.setIsGroup(conversationsDetails.getIsGroup());
                }
                if (conversationsDetails.getCreatedBy() != null) {
                    existingConversationsDetails.setCreatedBy(conversationsDetails.getCreatedBy());
                }
                if (conversationsDetails.getCreatedOn() != null) {
                    existingConversationsDetails.setCreatedOn(conversationsDetails.getCreatedOn());
                }
                if (conversationsDetails.getModifiedBy() != null) {
                    existingConversationsDetails.setModifiedBy(conversationsDetails.getModifiedBy());
                }
                if (conversationsDetails.getModifiedOn() != null) {
                    existingConversationsDetails.setModifiedOn(conversationsDetails.getModifiedOn());
                }

                return existingConversationsDetails;
            })
            .map(conversationsDetailsRepository::save);
    }

    /**
     * Get all the conversationsDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConversationsDetails> findAll(Pageable pageable) {
        log.debug("Request to get all ConversationsDetails");
        return conversationsDetailsRepository.findAll(pageable);
    }

    /**
     * Get one conversationsDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConversationsDetails> findOne(Long id) {
        log.debug("Request to get ConversationsDetails : {}", id);
        return conversationsDetailsRepository.findById(id);
    }

    /**
     * Delete the conversationsDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConversationsDetails : {}", id);
        conversationsDetailsRepository.deleteById(id);
    }
}
