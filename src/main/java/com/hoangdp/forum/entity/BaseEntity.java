package com.hoangdp.forum.entity;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {
    private Date createOn;
    private UUID createdBy;
    private Date lastModifiedOn;
    private UUID lastModifiedBy;
}
