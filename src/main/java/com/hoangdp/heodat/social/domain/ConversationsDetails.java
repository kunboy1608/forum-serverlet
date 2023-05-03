package com.hoangdp.heodat.social.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConversationsDetails.
 */
@Entity
@Table(name = "conversations_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversationsDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_group")
    private Boolean isGroup;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_on")
    private Instant modifiedOn;

    @ManyToOne
    @JsonIgnoreProperties(value = { "conversationsDetails" }, allowSetters = true)
    private Conversations conversations;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConversationsDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ConversationsDetails name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsGroup() {
        return this.isGroup;
    }

    public ConversationsDetails isGroup(Boolean isGroup) {
        this.setIsGroup(isGroup);
        return this;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ConversationsDetails createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public ConversationsDetails createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public ConversationsDetails modifiedBy(String modifiedBy) {
        this.setModifiedBy(modifiedBy);
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedOn() {
        return this.modifiedOn;
    }

    public ConversationsDetails modifiedOn(Instant modifiedOn) {
        this.setModifiedOn(modifiedOn);
        return this;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Conversations getConversations() {
        return this.conversations;
    }

    public void setConversations(Conversations conversations) {
        this.conversations = conversations;
    }

    public ConversationsDetails conversations(Conversations conversations) {
        this.setConversations(conversations);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConversationsDetails)) {
            return false;
        }
        return id != null && id.equals(((ConversationsDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConversationsDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isGroup='" + getIsGroup() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            "}";
    }
}
