package com.hoangdp.forum.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class BaseEntity {
    private Date createOn;
    private String createdBy;
    private Date lastModifiedOn;
    private String lastModifiedBy;
}
