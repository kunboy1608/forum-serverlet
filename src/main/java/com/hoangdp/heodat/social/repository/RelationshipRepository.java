package com.hoangdp.heodat.social.repository;

import com.hoangdp.heodat.social.domain.Relationship;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Relationship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {}
