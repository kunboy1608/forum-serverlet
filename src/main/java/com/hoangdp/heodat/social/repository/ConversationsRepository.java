package com.hoangdp.heodat.social.repository;

import com.hoangdp.heodat.social.domain.Conversations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Conversations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversationsRepository extends JpaRepository<Conversations, Long> {}
