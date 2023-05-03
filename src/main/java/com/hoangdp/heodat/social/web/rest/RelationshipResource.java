package com.hoangdp.heodat.social.web.rest;

import com.hoangdp.heodat.social.domain.Relationship;
import com.hoangdp.heodat.social.repository.RelationshipRepository;
import com.hoangdp.heodat.social.service.RelationshipService;
import com.hoangdp.heodat.social.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.hoangdp.heodat.social.domain.Relationship}.
 */
@RestController
@RequestMapping("/api")
public class RelationshipResource {

    private final Logger log = LoggerFactory.getLogger(RelationshipResource.class);

    private static final String ENTITY_NAME = "socialHeoDatRelationship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelationshipService relationshipService;

    private final RelationshipRepository relationshipRepository;

    public RelationshipResource(RelationshipService relationshipService, RelationshipRepository relationshipRepository) {
        this.relationshipService = relationshipService;
        this.relationshipRepository = relationshipRepository;
    }

    /**
     * {@code POST  /relationships} : Create a new relationship.
     *
     * @param relationship the relationship to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relationship, or with status {@code 400 (Bad Request)} if the relationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/relationships")
    public ResponseEntity<Relationship> createRelationship(@RequestBody Relationship relationship) throws URISyntaxException {
        log.debug("REST request to save Relationship : {}", relationship);
        if (relationship.getRelationshipId() != null) {
            throw new BadRequestAlertException("A new relationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Relationship result = relationshipService.save(relationship);
        return ResponseEntity
            .created(new URI("/api/relationships/" + result.getRelationshipId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRelationshipId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /relationships/:relationshipId} : Updates an existing relationship.
     *
     * @param relationshipId the id of the relationship to save.
     * @param relationship the relationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relationship,
     * or with status {@code 400 (Bad Request)} if the relationship is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/relationships/{relationshipId}")
    public ResponseEntity<Relationship> updateRelationship(
        @PathVariable(value = "relationshipId", required = false) final Long relationshipId,
        @RequestBody Relationship relationship
    ) throws URISyntaxException {
        log.debug("REST request to update Relationship : {}, {}", relationshipId, relationship);
        if (relationship.getRelationshipId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(relationshipId, relationship.getRelationshipId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relationshipRepository.existsById(relationshipId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Relationship result = relationshipService.update(relationship);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relationship.getRelationshipId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /relationships/:relationshipId} : Partial updates given fields of an existing relationship, field will ignore if it is null
     *
     * @param relationshipId the id of the relationship to save.
     * @param relationship the relationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relationship,
     * or with status {@code 400 (Bad Request)} if the relationship is not valid,
     * or with status {@code 404 (Not Found)} if the relationship is not found,
     * or with status {@code 500 (Internal Server Error)} if the relationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/relationships/{relationshipId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Relationship> partialUpdateRelationship(
        @PathVariable(value = "relationshipId", required = false) final Long relationshipId,
        @RequestBody Relationship relationship
    ) throws URISyntaxException {
        log.debug("REST request to partial update Relationship partially : {}, {}", relationshipId, relationship);
        if (relationship.getRelationshipId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(relationshipId, relationship.getRelationshipId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relationshipRepository.existsById(relationshipId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Relationship> result = relationshipService.partialUpdate(relationship);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relationship.getRelationshipId().toString())
        );
    }

    /**
     * {@code GET  /relationships} : get all the relationships.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relationships in body.
     */
    @GetMapping("/relationships")
    public ResponseEntity<List<Relationship>> getAllRelationships(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Relationships");
        Page<Relationship> page = relationshipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /relationships/:id} : get the "id" relationship.
     *
     * @param id the id of the relationship to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relationship, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/relationships/{id}")
    public ResponseEntity<Relationship> getRelationship(@PathVariable Long id) {
        log.debug("REST request to get Relationship : {}", id);
        Optional<Relationship> relationship = relationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relationship);
    }

    /**
     * {@code DELETE  /relationships/:id} : delete the "id" relationship.
     *
     * @param id the id of the relationship to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/relationships/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long id) {
        log.debug("REST request to delete Relationship : {}", id);
        relationshipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
