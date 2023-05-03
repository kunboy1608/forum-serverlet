package com.hoangdp.heodat.social.repository;

import com.hoangdp.heodat.social.domain.ConversationsDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConversationsDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversationsDetailsRepository extends JpaRepository<ConversationsDetails, Long> {}
