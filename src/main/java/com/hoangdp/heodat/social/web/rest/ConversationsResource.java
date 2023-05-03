package com.hoangdp.heodat.social.web.rest;

import com.hoangdp.heodat.social.domain.Conversations;
import com.hoangdp.heodat.social.repository.ConversationsRepository;
import com.hoangdp.heodat.social.service.ConversationsService;
import com.hoangdp.heodat.social.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hoangdp.heodat.social.domain.Conversations}.
 */
@RestController
@RequestMapping("/api")
public class ConversationsResource {

    private final Logger log = LoggerFactory.getLogger(ConversationsResource.class);

    private static final String ENTITY_NAME = "socialHeoDatConversations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConversationsService conversationsService;

    private final ConversationsRepository conversationsRepository;

    public ConversationsResource(ConversationsService conversationsService, ConversationsRepository conversationsRepository) {
        this.conversationsService = conversationsService;
        this.conversationsRepository = conversationsRepository;
    }

    /**
     * {@code POST  /conversations} : Create a new conversations.
     *
     * @param conversations the conversations to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversations, or with status {@code 400 (Bad Request)} if the conversations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conversations")
    public ResponseEntity<Conversations> createConversations(@Valid @RequestBody Conversations conversations) throws URISyntaxException {
        log.debug("REST request to save Conversations : {}", conversations);
        if (conversations.getConversationId() != null) {
            throw new BadRequestAlertException("A new conversations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conversations result = conversationsService.save(conversations);
        return ResponseEntity
            .created(new URI("/api/conversations/" + result.getConversationId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getConversationId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conversations/:conversationId} : Updates an existing conversations.
     *
     * @param conversationId the id of the conversations to save.
     * @param conversations the conversations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversations,
     * or with status {@code 400 (Bad Request)} if the conversations is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conversations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conversations/{conversationId}")
    public ResponseEntity<Conversations> updateConversations(
        @PathVariable(value = "conversationId", required = false) final Long conversationId,
        @Valid @RequestBody Conversations conversations
    ) throws URISyntaxException {
        log.debug("REST request to update Conversations : {}, {}", conversationId, conversations);
        if (conversations.getConversationId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(conversationId, conversations.getConversationId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationsRepository.existsById(conversationId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Conversations result = conversationsService.update(conversations);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conversations.getConversationId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conversations/:conversationId} : Partial updates given fields of an existing conversations, field will ignore if it is null
     *
     * @param conversationId the id of the conversations to save.
     * @param conversations the conversations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversations,
     * or with status {@code 400 (Bad Request)} if the conversations is not valid,
     * or with status {@code 404 (Not Found)} if the conversations is not found,
     * or with status {@code 500 (Internal Server Error)} if the conversations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conversations/{conversationId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Conversations> partialUpdateConversations(
        @PathVariable(value = "conversationId", required = false) final Long conversationId,
        @NotNull @RequestBody Conversations conversations
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conversations partially : {}, {}", conversationId, conversations);
        if (conversations.getConversationId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(conversationId, conversations.getConversationId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationsRepository.existsById(conversationId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conversations> result = conversationsService.partialUpdate(conversations);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conversations.getConversationId().toString())
        );
    }

    /**
     * {@code GET  /conversations} : get all the conversations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversations in body.
     */
    @GetMapping("/conversations")
    public ResponseEntity<List<Conversations>> getAllConversations(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Conversations");
        Page<Conversations> page = conversationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conversations/:id} : get the "id" conversations.
     *
     * @param id the id of the conversations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conversations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conversations/{id}")
    public ResponseEntity<Conversations> getConversations(@PathVariable Long id) {
        log.debug("REST request to get Conversations : {}", id);
        Optional<Conversations> conversations = conversationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conversations);
    }

    /**
     * {@code DELETE  /conversations/:id} : delete the "id" conversations.
     *
     * @param id the id of the conversations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Void> deleteConversations(@PathVariable Long id) {
        log.debug("REST request to delete Conversations : {}", id);
        conversationsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
