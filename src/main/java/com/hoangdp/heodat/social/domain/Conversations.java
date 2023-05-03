package com.hoangdp.heodat.social.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conversations.
 */
@Entity
@Table(name = "conversations")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Conversations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long conversationId;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Column(name = "sender", nullable = false)
    private Long sender;

    @NotNull
    @Column(name = "receiver", nullable = false)
    private Long receiver;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @OneToMany(mappedBy = "conversations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversations" }, allowSetters = true)
    private Set<ConversationsDetails> conversationsDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getConversationId() {
        return this.conversationId;
    }

    public Conversations conversationId(Long conversationId) {
        this.setConversationId(conversationId);
        return this;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Conversations timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSender() {
        return this.sender;
    }

    public Conversations sender(Long sender) {
        this.setSender(sender);
        return this;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return this.receiver;
    }

    public Conversations receiver(Long receiver) {
        this.setReceiver(receiver);
        return this;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return this.message;
    }

    public Conversations message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<ConversationsDetails> getConversationsDetails() {
        return this.conversationsDetails;
    }

    public void setConversationsDetails(Set<ConversationsDetails> conversationsDetails) {
        if (this.conversationsDetails != null) {
            this.conversationsDetails.forEach(i -> i.setConversations(null));
        }
        if (conversationsDetails != null) {
            conversationsDetails.forEach(i -> i.setConversations(this));
        }
        this.conversationsDetails = conversationsDetails;
    }

    public Conversations conversationsDetails(Set<ConversationsDetails> conversationsDetails) {
        this.setConversationsDetails(conversationsDetails);
        return this;
    }

    public Conversations addConversationsDetails(ConversationsDetails conversationsDetails) {
        this.conversationsDetails.add(conversationsDetails);
        conversationsDetails.setConversations(this);
        return this;
    }

    public Conversations removeConversationsDetails(ConversationsDetails conversationsDetails) {
        this.conversationsDetails.remove(conversationsDetails);
        conversationsDetails.setConversations(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conversations)) {
            return false;
        }
        return conversationId != null && conversationId.equals(((Conversations) o).conversationId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conversations{" +
            "conversationId=" + getConversationId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", sender=" + getSender() +
            ", receiver=" + getReceiver() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
